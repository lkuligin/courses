#!/usr/bin/env python
"""
movie_recommendations.py
Original author: Felix Sun (6.008 TA, Fall 2015)
Modified by:
- Danielle Pace (6.008 TA, Fall 2016),
- George H. Chen (6.008/6.008.1x instructor, Fall 2016)

"""

import matplotlib.pyplot as plt
import movie_data_helper
import numpy as np
import scipy
import scipy.misc
from sys import exit


def compute_posterior(prior, likelihood, y):
    """
    Use Bayes' rule for random variables to compute the posterior distribution
    of a hidden variable X, given N observations Y_0, Y_1, ..., Y_{N-1}.
    Conditioned on X, these observations Y_0, Y_1, ..., Y_{N-1} are i.i.d.

    Hidden random variable X is assumed to take on a value in {0, 1, ..., M-1}.

    Each random variable Y_i takes on a value in {0, 1, ..., K-1}.

    Inputs
    ------
    - prior: a length M vector stored as a 1D NumPy array; prior[m] gives the
        (unconditional) probability that X = m
    - likelihood: a K row by M column matrix stored as a 2D NumPy array;
        likelihood[k, m] gives the probability that Y = k given X = m
    - y: a length-N vector stored as a 1D NumPy array; y[n] gives the observed
        value for random variable Y_n

    Output
    ------
    - posterior: a length M vector stored as a 1D NumPy array: posterior[m]
        gives the probability that X = m given
        Y_0 = y_0, ..., Y_{N-1} = y_{n-1}
    """

    # check that prior probabilities sum to 1
    if np.abs(1 - np.sum(prior)) > 1e-06:
        exit('In compute_posterior: The prior probabilities need to sum to 1')

    # check that likelihood is specified as a 2D array
    if len(likelihood.shape) != 2:
        exit('In compute_posterior: The likelihood needs to be specified as ' +
             'a 2D array')

    K, M = likelihood.shape

    if len(prior) != M:
        exit('In compute_posterior: Mismatch in number of hidden states ' +
             'according to the prior and the likelihood.')

    for m in range(M):
        if np.abs(1 - np.sum(likelihood[:, m])) > 1e-06:
            exit('In compute_posterior: P(Y | X = %d) does not sum to 1' % m)

    log_prior = np.log(prior)
    log_likelihood = np.log(likelihood)
    log_conditional = np.sum([log_likelihood[i,:] for i in y], axis = 0)
    log_answer = log_prior + log_conditional - scipy.misc.logsumexp(log_prior + log_conditional, axis = 0)

    posterior = np.exp(log_answer)
    return posterior


def compute_movie_rating_likelihood(M):
    """
    Compute the rating likelihood probability distribution of Y given X where
    Y is an individual rating (takes on a value in {0, 1, ..., M-1}), and X
    is the hidden true/inherent rating of a movie (also takes on a value in
    {0, 1, ..., M-1}).

    Please refer to the instructions of the project to see what the
    likelihood for ratings should be.

    Output
    ------
    - likelihood: an M row by M column matrix stored as a 2D NumPy array;
        likelihood[k, m] gives the probability that Y = k given X = m
    """

    likelihood = np.zeros((M, M))

    for i in range(M):
        for j in range(M):
            if i == j:
                likelihood[i,j] = 2.
            else:
                likelihood[i,j] = 1./np.abs(i-j)
    alpha = np.sum(likelihood, axis = 0)
    alpha = 1./alpha
    likelihood = alpha * likelihood

    return likelihood


def infer_true_movie_ratings(num_observations=-1):
    """
    For every movie, computes the posterior distribution and MAP estimate of
    the movie's true/inherent rating given the movie's observed ratings.

    Input
    -----
    - num_observations: integer that specifies how many available ratings to
        use per movie (the default value of -1 indicates that all available
        ratings will be used).

    Output
    ------
    - posteriors: a 2D array consisting of the posterior distributions where
        the number of rows is the number of movies, and the number of columns
        is M, i.e., the number of possible ratings (remember ratings are
        0, 1, ..., M-1); posteriors[i] gives a length M vector that is the
        posterior distribution of the true/inherent rating of the i-th movie
        given ratings for the i-th movie (where for each movie, the number of
        observations used is precisely what is specified by the input variable
        `num_observations`)
    - MAP_ratings: a 1D array with length given by the number of movies;
        MAP_ratings[i] gives the true/inherent rating with the highest
        posterior probability in the distribution `posteriors[i]`
    """

    M = 11
    prior = np.array([1.0 / M] * M)  # uniform distribution
    likelihood = compute_movie_rating_likelihood(M)

    movie_id_list = movie_data_helper.get_movie_id_list()
    num_movies = len(movie_id_list)

    posteriors = np.zeros((num_movies, M))
    MAP_ratings = np.zeros(num_movies)

    for id in movie_id_list:
        ratings = movie_data_helper.get_ratings(id)
        if num_observations > 0:
            ratings = ratings[:num_observations]
        posteriors_ = compute_posterior(prior, likelihood, ratings)
        posteriors[id] = posteriors_
        MAP_ratings[id] = np.argmax(posteriors_, axis = 0)

    return posteriors, MAP_ratings


def compute_entropy(distribution):
    """
    Given a distribution, computes the Shannon entropy of the distribution in
    bits.

    Input
    -----
    - distribution: a 1D array of probabilities that sum to 1

    Output:
    - entropy: the Shannon entropy of the input distribution in bits
    """

    #
    if np.abs(1 - np.sum(distribution)) > 1e-6:
        exit('In compute_entropy: distribution should sum to 1.')

    entropy = -np.sum([i*np.log(i)/np.log(2.) if i > 0 else 0. for i in distribution])

    return entropy


def compute_true_movie_rating_posterior_entropies(num_observations):
    """
    For every movie, computes the Shannon entropy (in bits) of the posterior
    distribution of the true/inherent rating of the movie given observed
    ratings.

    Input
    -----
    - num_observations: integer that specifies how many available ratings to
        use per movie (the default value of -1 indicates that all available
        ratings will be used)

    Output
    ------
    - posterior_entropies: a 1D array; posterior_entropies[i] gives the Shannon
        entropy (in bits) of the posterior distribution of the true/inherent
        rating of the i-th movie given observed ratings (with number of
        observed ratings given by the input `num_observations`)
    """

    posteriors, _ = infer_true_movie_ratings(num_observations)
    posterior_entropies = []
    for el in posteriors:
        posterior_entropies.append(compute_entropy(el))

    return posterior_entropies


def main():

    print("Posterior calculation (few observations)")
    prior = np.array([0.6, 0.4])
    likelihood = np.array([
        [0.7, 0.98],
        [0.3, 0.02],
    ])
    y = [0]*2 + [1]*1
    print("My answer:")
    print(compute_posterior(prior, likelihood, y))
    print("Expected answer:")
    print(np.array([[0.91986917, 0.08013083]]))

    print("---")
    print("Entropy of fair coin flip")
    distribution = np.array([0.5, 0.5])
    print("My answer:")
    print(compute_entropy(distribution))
    print("Expected answer:")
    print(1.0)

    print("Entropy of coin flip where P(heads) = 0.25 and P(tails) = 0.75")
    distribution = np.array([0.25, 0.75])
    print("My answer:")
    print(compute_entropy(distribution))
    print("Expected answer:")
    print(0.811278124459)

    print("Entropy of coin flip where P(heads) = 0.75 and P(tails) = 0.25")
    distribution = np.array([0.75, 0.25])
    print("My answer:")
    print(compute_entropy(distribution))
    print("Expected answer:")
    print(0.811278124459)

if __name__ == '__main__':
    main()
