#!/usr/bin/env python
# inference.py
# Base code by George H. Chen (georgehc@mit.edu) -- updated 10/18/2016
import collections
import sys

import graphics
import numpy as np
import robot


# Throughout the code, we use these variables.
# Do NOT change these (but you'll need to use them!):
# - all_possible_hidden_states: a list of possible hidden states
# - all_possible_observed_states: a list of possible observed states
# - prior_distribution: a distribution over states
# - transition_model: a function that takes a hidden state and returns a
#     Distribution for the next state
# - observation_model: a function that takes a hidden state and returns a
#     Distribution for the observation from that hidden state
all_possible_hidden_states = robot.get_all_hidden_states()
all_possible_observed_states = robot.get_all_observed_states()
prior_distribution = robot.initial_distribution()
transition_model = robot.transition_model
observation_model = robot.observation_model


# You may find this function helpful for computing logs without yielding a
# NumPy warning when taking the log of 0.
def careful_log(x):
    # computes the log of a non-negative real number
    if x == 0:
        return -np.inf
    else:
        return np.log(x)


# -----------------------------------------------------------------------------
# Functions for you to implement
#

def forward_backward(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of marginal distributions at each time step; each distribution
    should be encoded as a Distribution (see the Distribution class in
    robot.py and see how it is used in both robot.py and the function
    generate_data() above, and the i-th Distribution should correspond to time
    step i
    """
    num_time_steps = len(observations)
    forward_messages = [None] * num_time_steps

    B = {}
    state_from_observation = {} #dictionary {observation: [state1, state2, ...]} - for each observation all states from which it can be observed
    for state in all_possible_hidden_states:
        model = observation_model(state)
        for obs in model.keys():
            if obs not in state_from_observation:
                state_from_observation[obs] = []
            state_from_observation[obs].append(state)
            B[(state, obs)] = model[obs] 
    #previous_states = {}
    next_states = {}
    A = {}
    for state in all_possible_hidden_states:
        transition_matrix = transition_model(state)
        if state not in next_states:
            next_states[state] = []
        for state_next in transition_matrix.keys():
            #if state_next not in previous_states:
            #    previous_states[state_next] = []
            #previous_states[state_next].append(state)
            next_states[state].append(state_next)
            A[(state, state_next)] = transition_matrix[state_next]

    #computing forward messages
    states_amount = len(all_possible_hidden_states)
    #forward_messages[0] = prior_distribution
    #forward_messages[0] = robot.Distribution()
    forward_messages[0] = prior_distribution
    for i in range(1, num_time_steps, 1):
        observation = observations[i-1]
        forward_messages[i] = robot.Distribution()
        if observation:
            for state_next in all_possible_hidden_states:
                for state_current in state_from_observation[observation]:
                    m = forward_messages[i-1].get(state_current,0) * A.get((state_current,state_next),0) * B.get((state_current, observation), 0)
                    if m > 0:               
                        forward_messages[i][state_next] += m
        else:
            for state_next in all_possible_hidden_states:
                for state_current in forward_messages[i-1].keys():
                    m = forward_messages[i-1].get(state_current,0) * A.get((state_current,state_next),0)
                    if m > 0:               
                        forward_messages[i][state_next] += m
        forward_messages[i].renormalize()
        #print i, forward_messages[i]

    backward_messages = [None] * num_time_steps
    backward_messages[num_time_steps-1] = robot.Distribution()
    for state in all_possible_hidden_states:
        backward_messages[num_time_steps-1][state] = 1./states_amount
    for i in range(num_time_steps-2, -1, -1):
        observation = observations[i+1]
        backward_messages[i] = robot.Distribution()
        if observation:
            for state_current in all_possible_hidden_states:
                for state_next in state_from_observation[observation]:
                    m = B.get((state_next, observation), 0) * A.get((state_current, state_next), 0) * backward_messages[i+1].get(state_next, 0)
                    if m > 0:
                        backward_messages[i][state_current] += m
        else:
             for state_current in all_possible_hidden_states:
                for state_next in backward_messages[i+1].keys():
                    m = A.get((state_current, state_next), 0) * backward_messages[i+1].get(state_next, 0)
                    if m > 0:
                        backward_messages[i][state_current] += m
        backward_messages[i].renormalize()
        #print observation, i, backward_messages[i]
    
    marginals = [None] * num_time_steps # remove this
    marginals[0] = robot.Distribution()
    for state in backward_messages[0]:
        m = backward_messages[0].get(state,0) * B.get((state, observations[0]), 0) * prior_distribution.get(state, 0)
        if m > 0:
            marginals[0][state] = m
    marginals[0].renormalize()
    
    marginals[num_time_steps-1] = robot.Distribution()
    observation = observations[num_time_steps-1]
    for state in forward_messages[num_time_steps-1]:
        if observation:
            m = forward_messages[num_time_steps-1].get(state,0) * B.get((state, observation), 0)
        else:
            m = forward_messages[num_time_steps-1].get(state,0)
        if m > 0:
            marginals[num_time_steps-1][state] = m
    marginals[num_time_steps-1].renormalize()
    
    for i in range(1, num_time_steps-1, 1):
        observation = observations[i]
        marginals[i] = robot.Distribution()
        if observation:
            for state in state_from_observation[observation]:
                m = B.get((state, observation), 0) * forward_messages[i].get(state,0) * backward_messages[i].get(state,0)
                if m > 0:
                    marginals[i][state] = m
        else:
            for state in backward_messages[i]:
                m = forward_messages[i].get(state,0) * backward_messages[i].get(state,0)
                if m > 0:
                    marginals[i][state] = m
        marginals[i].renormalize()
    
    #print observation_model((3,3,'stay'))
    #print transition_model((3,3,'stay'))
    #print observation_model((3,3,'up'))
    #print transition_model((3,3,'up'))

    return marginals


def Viterbi(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of esimated hidden states, each encoded as a tuple
    (<x>, <y>, <action>)
    """

    B = {}
    state_from_observation = {} 
    for state in all_possible_hidden_states:
        model = observation_model(state)
        for obs in model.keys():
            if obs not in state_from_observation:
                state_from_observation[obs] = []
            state_from_observation[obs].append(state)
            B[(state, obs)] = model[obs] 

    next_states = {}
    A = {}
    for state in all_possible_hidden_states:
        transition_matrix = transition_model(state)
        for state_next in transition_matrix.keys():
            A[(state, state_next)] = transition_matrix[state_next]

    states_amount = len(all_possible_hidden_states)

    num_time_steps = len(observations)
    estimated_hidden_states = [None] * num_time_steps # remove this

    forward_messages = [None] * num_time_steps
    backward_messages = [None] * num_time_steps
    forward_messages[0] = prior_distribution
    for i in range(1, num_time_steps, 1):
        observation = observations[i-1]
        forward_messages[i] = {}
        backward_messages[i] = {}
        m = {}
        for state_next in all_possible_hidden_states:
            min_ = np.inf
            state_min = None
            if observation:
                for state_current in state_from_observation[observation]:
                    tmp = np.inf
                    if (i > 1) | (state_current in prior_distribution):
                        tmp = (forward_messages[i-1].get(state_current,0)
                             - careful_log(A.get((state_current,state_next),0))
                            - careful_log(B.get((state_current, observation), 0)))
                    if tmp < min_:
                        state_min = state_current
                        min_ = tmp
            else:
                for state_current in forward_messages[i-1].keys():
                    tmp = np.inf
                    tmp = (forward_messages[i-1].get(state_current,0)
                        - careful_log(A.get((state_current,state_next),0)))
                    if tmp < min_:
                        state_min = state_current
                        min_ = tmp
            forward_messages[i][state_next] = min_
            if state_min:
                backward_messages[i][state_next] = state_min
        #forward_messages[i].renormalize()

    #print forward_messages[1]

    observation = observations[num_time_steps-1]
    min_ = np.inf
    state_min = None
    for state in state_from_observation[observation]:
        tmp = (forward_messages[num_time_steps-1].get(state,0)
            - careful_log(B.get((state, observation), 0)))
        if tmp < min_:
            min_ = tmp
            state_min = state
    #print state_min
    estimated_hidden_states[num_time_steps-1] = state_min
    for i in range(num_time_steps-2, -1, -1):
        estimated_hidden_states[i] = backward_messages[i+1].get(estimated_hidden_states[i+1], None)

    return estimated_hidden_states


def second_best(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of esimated hidden states, each encoded as a tuple
    (<x>, <y>, <action>)
    """

    # -------------------------------------------------------------------------
    # YOUR CODE GOES HERE
    #


    B = {}
    state_from_observation = {} 
    for state in all_possible_hidden_states:
        model = observation_model(state)
        for obs in model.keys():
            if obs not in state_from_observation:
                state_from_observation[obs] = []
            state_from_observation[obs].append(state)
            B[(state, obs)] = model[obs] 

    next_states = {}
    A = {}
    for state in all_possible_hidden_states:
        transition_matrix = transition_model(state)
        for state_next in transition_matrix.keys():
            A[(state, state_next)] = transition_matrix[state_next]

    states_amount = len(all_possible_hidden_states)

    num_time_steps = len(observations)
    estimated_hidden_states = [None] * num_time_steps # remove this

    forward_messages = [None] * num_time_steps
    forward_messages2 = [None] * num_time_steps
    backward_messages = [None] * num_time_steps
    backward_messages2 = [None] * num_time_steps
    
    forward_messages[0] = prior_distribution
    forward_messages2[0] = prior_distribution
    for i in range(1, num_time_steps, 1):
        observation = observations[i-1]
        forward_messages[i] = {}
        forward_messages2[i] = {}
        backward_messages[i] = {}
        backward_messages2[i] = {}

        m = {}
        for state_next in all_possible_hidden_states:
            min_ = np.inf
            min2_ = np.inf
            state_min = None
            state_min_2 = None
            if observation:
                for state_current in state_from_observation[observation]:
                    tmp = np.inf
                    tmp2 = np.inf
                    if (i > 1) | (state_current in prior_distribution):
                        tmp = (forward_messages[i-1].get(state_current,0)
                             - careful_log(A.get((state_current,state_next),0))
                            - careful_log(B.get((state_current, observation), 0)))
                        tmp2 = (forward_messages2[i-1].get(state_current,0)
                             - careful_log(A.get((state_current,state_next),0))
                            - careful_log(B.get((state_current, observation), 0)))
                    if tmp < min_:
                        #min2_ = min_
                        min_ = tmp
                        #state_min_2 = state_min
                        state_min = state_current
                    if tmp2 < min2_:
                    #    min2_ = tmp2
                    #    state_min_2 = state_current
            else:
                for state_current in forward_messages[i-1].keys():
                    tmp = np.inf
                    tmp2 = np.inf
                    tmp = (forward_messages[i-1].get(state_current,0)
                        - careful_log(A.get((state_current,state_next),0)))
                    tmp2 = (forward_messages2[i-1].get(state_current,0)
                        - careful_log(A.get((state_current,state_next),0)))
                    if tmp < min_:
                        min2_ = min_
                        min_ = tmp
                        state_min_2 = state_min
                        state_min = state_current
                    #if tmp2 < min2_:
                    #    min2_ = tmp2
                    #    state_min_2 = state_current
            forward_messages[i][state_next] = min_
            forward_messages2[i][state_next] = min2_
            if state_min_2:
                backward_messages[i][state_next] = state_min_2
            elif state_min:
                backward_messages[i][state_next] = state_min
        #forward_messages[i].renormalize()

    #print forward_messages[1]

    observation = observations[num_time_steps-1]
    min_ = np.inf
    #min_2 = np.inf
    state_min = None
    #state_min_2 = None
    for state in state_from_observation[observation]:
        tmp = (forward_messages[num_time_steps-1].get(state,0)
            - careful_log(B.get((state, observation), 0)))
        if tmp < min_:
            min_2 = min_
            min_ = tmp
            state_min_2 = state_min
            state_min = state
    #print backward_messages
    estimated_hidden_states[num_time_steps-1] = state_min_2
    for i in range(num_time_steps-2, -1, -1):
        estimated_hidden_states[i] = backward_messages[i+1].get(estimated_hidden_states[i+1], None)

    return estimated_hidden_states

# -----------------------------------------------------------------------------
# Generating data from the hidden Markov model
#

def generate_data(num_time_steps, make_some_observations_missing=False,
                  random_seed=None):
    # generate samples from this project's hidden Markov model
    hidden_states = []
    observations = []

    # if the random seed is not None, then this makes the randomness
    # deterministic, which may be helpful for debug purposes
    np.random.seed(random_seed)

    # draw initial state and emit an observation
    initial_state = prior_distribution.sample()
    initial_observation = observation_model(initial_state).sample()

    hidden_states.append(initial_state)
    observations.append(initial_observation)

    for time_step in range(1, num_time_steps):
        # move the robot
        prev_state = hidden_states[-1]
        new_state = transition_model(prev_state).sample()

        # maybe emit an observation
        if not make_some_observations_missing:
            new_observation = observation_model(new_state).sample()
        else:
            if np.random.rand() < .1:  # 0.1 prob. of observation being missing
                new_observation = None
            else:
                new_observation = observation_model(new_state).sample()

        hidden_states.append(new_state)
        observations.append(new_observation)

    return hidden_states, observations


# -----------------------------------------------------------------------------
# Main
#

def main():
    # flags
    make_some_observations_missing = False
    use_graphics = True
    need_to_generate_data = True

    # parse command line arguments
    for arg in sys.argv[1:]:
        if arg == '--missing':
            make_some_observations_missing = True
        elif arg == '--nographics':
            use_graphics = False
        elif arg.startswith('--load='):
            filename = arg[7:]
            hidden_states, observations = robot.load_data(filename)
            need_to_generate_data = False
            num_time_steps = len(hidden_states)

    # if no data is loaded, then generate new data
    if need_to_generate_data:
        num_time_steps = 100
        hidden_states, observations = \
            generate_data(num_time_steps,
                          make_some_observations_missing)

    print('Running forward-backward...')
    marginals = forward_backward(observations)
    print("\n")

    timestep = 99
    print("Most likely parts of marginal at time %d:" % (timestep))
    if marginals[timestep] is not None:
        print(sorted(marginals[timestep].items(),
                     key=lambda x: x[1],
                     reverse=True)[:10])
    else:
        print('*No marginal computed*')
    print("\n")

    print('Running Viterbi...')
    estimated_states = Viterbi(observations)
    print("\n")

    print("Last 10 hidden states in the MAP estimate:")
    for time_step in range(num_time_steps - 10 - 1, num_time_steps):
        if estimated_states[time_step] is None:
            print('Missing')
        else:
            print(estimated_states[time_step])
    print("\n")

    print('Finding second-best MAP estimate...')
    estimated_states2 = second_best(observations)
    print("\n")

    print("Last 10 hidden states in the second-best MAP estimate:")
    for time_step in range(num_time_steps - 10 - 1, num_time_steps):
        if estimated_states2[time_step] is None:
            print('Missing')
        else:
            print(estimated_states2[time_step])
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states[time_step] != hidden_states[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between MAP estimate and true hidden " +
          "states:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states2[time_step] != hidden_states[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between second-best MAP estimate and " +
          "true hidden states:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states[time_step] != estimated_states2[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between MAP and second-best MAP " +
          "estimates:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    # display
    if use_graphics:
        app = graphics.playback_positions(hidden_states,
                                          observations,
                                          estimated_states,
                                          marginals)
        app.mainloop()


if __name__ == '__main__':
    main()
