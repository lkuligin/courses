library(ggplot2)
library(tidyr)
library(plyr)
library(dplyr)
library(gridExtra)

data(diamonds)
summary(diamonds)
nrow(diamonds)
ncol(diamonds)
?diamonds

ggplot(aes(price), data=diamonds) + 
  geom_histogram(binwidth = 50) +
  #xlim(350, 3000) + 
  facet_wrap(~ cut)
mean(diamonds$price)
median(diamonds$price)

nrow(subset(diamonds, diamonds$price >= 15000))
table(diamonds$cut)
by(diamonds$price, diamonds$cut, summary)

ggplot(aes(x=color, y=price/carat), data=diamonds) + 
  geom_boxplot() 
quantile(subset(diamonds$price, diamonds$color=='J'), probs = seq(0, 1, 0.25))
IQR(subset(diamonds$price, diamonds$color=='D'))

table(diamonds$carat)

ggplot(aes(x=depth, y=price), data=diamonds) +
  geom_point(alpha=1/100) +
  scale_x_discrete(breaks = seq(44,80,2)) + 
cor(diamonds$price, diamonds$x)

ggplot(aes(x=carat, y=price), data=diamonds) +
  geom_point() +
  ylim(quantile(diamonds$price, 0.005),quantile(diamonds$price, 0.995)) +
  xlim(quantile(diamonds$carat, 0.005),quantile(diamonds$carat, 0.995))

ggplot(aes(x=x*y*z, y=price), data=diamonds) +
  geom_point()

ggplot(aes(x=carat, y=price), data=subset(diamonds, x*y*z > 0 & x*y*z < 800)) +
  geom_point()

#dplyr package example
diamondsByClarity <- diamonds %>%
  group_by(clarity) %>%
  summarise(mean_price = mean(price),
            median_price = median(price),
            min_price = min(price),
            max_price = max(price),
            n=n()) %>%
  arrange(clarity)

#grid with 2 barplots
p1 = ggplot(aes(x=)) +
  geom_bar()
grid.arrange(p2, p1, ncol = 1)

ggplot(aes(price), data=diamonds) +
  geom_histogram(aes(color=cut)) +
  facet_wrap(~color)

ggplot(aes(y=price,x=table), data=diamonds) +
  geom_point(aes(color=cut))

ggplot(aes(y=1.0*price/carat,x=cut), data=diamonds) +
  geom_point(aes(color=color)) +
  facet_wrap(~clarity)
  
ggplot(aes(x=x*y*z, y=price), data=diamonds) +
  geom_point()
