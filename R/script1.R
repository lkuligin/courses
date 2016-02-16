library(ggplot2)
library(gridExtra)
library(dplyr)
library(alr3)
library(tidyr)
library(reshape2)
library(GGally)

statesInfo <- read.csv('./EDA_Course_Materials/lesson2/stateData.csv')
subset(statesInfo, state.region == 1)
stateSubset <- statesInfo[statesInfo$state.region == 1, ]
head(stateSubset, 2)
dim(stateSubset)

reddit <- read.csv('./EDA_Course_Materials/lesson2/reddit.csv')
table(reddit$employment.status)
summary(reddit)
str(reddit)
levels(reddit$age.range)
qplot(data = reddit, x = reddit$age.range)
table(reddit$age.range)
reddit$age.range.ordered = ordered(reddit$age.range, levels = c("Under 18", 
                                                         "18-24", "25-34", "35-44"
                                                         , "55-64", "65 or Above"))
qplot(data = reddit, x = reddit$age.range.ordered)

getwd()
list.files()
pf <- read.csv("./EDA_Course_Materials/lesson3/pseudo_facebook.tsv", sep = "\t")
names(pf)
pf$dob_day = as.character(pf$dob_day)
qplot(x=dob_day, data = pf) + 
  scale_x_discrete(breaks=1:31)
#ggplot(aes(x = dob_day), data = pf) + geom_histogram() + scale_x_discrete(breaks = 1:31)
qplot(x=dob_day, data = pf) + 
  scale_x_discrete(breaks=1:31) +
  facet_wrap(~dob_month, ncol = 2)

qplot(x=friend_count, data = pf, xlim = c(0,1000))
qplot(x=friend_count, data = subset(pf, !is.na(gender)), binwidth = 25) +
  scale_x_continuous(limits = c(0,1000), 
                     breaks = seq(0,1000,50)) +
  facet_wrap(~gender)

qplot(x=friend_count, data = subset(pf, !is.na(gender)), binwidth = 10,
      geom = 'freqpoly', color = gender) +
  scale_x_continuous(limits = c(0,1000), 
                     breaks = seq(0,1000,50))
qplot(x = gender, y = friend_count, 
      data = subset(pf, !is.na(gender)), 
      geom = 'boxplot') +
  scale_y_continuous(limits = c(0,1000))

by(pf$friend_count, pf$gender, summary)
by(pf$www_likes, pf$gender, summary)
by(pf$www_likes, pf$gender, sum)
by(pf$friend_count, pf$gender, summary)

summary(pf$mobile_likes)
pf$mobile_check_in <- ifelse(pf$mobile_likes>0,1,0)
pf$mobile_check_in <- factor(pf$mobile_check_in)
summary(pf$mobile_check_in)
sum(pf$mobile_check_in == 1)/length(pf$mobile_check_in)

qplot(x=tenure, data = pf, color = I('black'), fill = I('#099DD9'))
qplot(x=tenure/365, data = pf, binwidth = 1, color = I('black'), fill = I('#099DD9'))
qplot(x=tenure/365, data = pf, binwidth = 0.25,
      xlab = "number of year using Facebook",
      ylab = "number of users in sample",
      color = I('black'), fill = I('#099DD9')) +
  scale_x_continuous(breaks = seq(1,7,1), limits = c(0,7))

pf$age = as.integer(pf$age)
qplot(x=age, data=pf, binwidth=1) + scale_x_continuous(breaks=seq(0,113,5))
p1 <- qplot(x = friend_count, data = pf)
p2 <- qplot(x = log10(friend_count+1), data = pf)
p3 <- qplot(x = sqrt(friend_count), data = pf)

grid.arrange(p1, p2, p3, ncol = 1)

qplot(x = age, y = friend_count, data = pf)
ggplot(aes(x=age, y=friend_count), data=pf) + 
  geom_point(alpha = 1/20, position = position_jitter(h = 0)) +
  xlim(13, 90) +
  coord_trans(y='sqrt')
ggplot(aes(x=age, y=friendships_initiated), data=pf) + 
  geom_point(alpha = 1/20, position = position_jitter(h = 0), color='orange') +
  xlim(13, 90) +
  coord_trans(y='sqrt') +
  geom_line(stat = "summary", fun.y=mean) +
  geom_line(stat = "summary", fun.y=quantile.0.1, 
            linetype=2, color='blue')

age_groups <- group_by(pf, age) 
pf.pfc_by_age <- summarise(age_groups,
          friend_count_mean = mean(friend_count),
          friend_count_median = median(friend_count),
          n = n())
pf.pfc_by_age <- arrange(pf.pfc_by_age, age)
head(pf.pfc_by_age)
ggplot(aes(x=age, y=friend_count_mean), data=pf.pfc_by_age) +
  geom_line()

cor.test(pf$age, pf$friend_count, method = "pearson")
with(pf, cor.test(age, friend_count, method = "pearson"))
with(subset(pf, age<70), cor.test(age, friend_count, method = "pearson"))

ggplot(aes(x=www_likes_received, y=likes_received), data=pf) +
  geom_point() + 
  xlim(0,quantile(pf$www_likes_received, 0.95)) + 
  ylim(0,quantile(pf$likes_received, 0.95)) + 
  geom_smooth(method='lm', color='red')

cor.test(pf$likes_received, pf$www_likes_received)

data("Mitchell")
?Mitchell
ggplot(aes(x=Month, y=Temp), data=Mitchell) +
  geom_point() +
  scale_x_discrete(breaks=seq(0, 203, 12))
cor.test(Mitchell$Month, Mitchell$Temp)

pf$age_with_months = pf$age + 1 - pf$dob_month/12
age_groups <- group_by(pf, age_with_months) 
pf.pfc_by_age_months <- pf %>%
  group_by(age_with_months) %>%
  summarise(friend_count_mean = mean(friend_count),
            friend_count_median = median(friend_count),
            n=n()) %>%
  arrange(age_with_months)
pf.fc_by_age <- pf %>%
  group_by(age) %>%
  summarise(friend_count_mean = mean(friend_count),
            friend_count_median = median(friend_count),
            n=n()) %>%
  arrange(age)


p1 <- ggplot(aes(x = age, y = friend_count_mean),
       data = subset(pf.fc_by_age, age<71)) +
  geom_line()
p2 <- ggplot(aes(x = age_with_months, y = friend_count_mean),
             data = subset(pf.pfc_by_age_months, age_with_months<71)) +
  geom_line()
p3 <- ggplot(aes(x = round(age/5)*5, y = friend_count_mean),
             data = subset(pf, age<71)) +
  geom_line(stat='summary', fun.y=mean)

grid.arrange(p2, p1, ncol=1)

  
        
ggplot(aes(x=gender, y=age),
       data=subset(pf, !is.na(gender))) +
  geom_boxplot() +
  stat_summary(fun.y=mean, geom = 'point', shape = 4)

ggplot(aes(x=age,y=friend_count),
       data=subset(pf, !is.na(gender))) +
  geom_line(aes(color = gender), stat = 'summary', fun.y = median)

pf.fc_by_age_gender <- pf %>%
  filter(!is.na(gender)) %>%
  group_by(age, gender) %>%
  summarise(mean_friend_count = mean(friend_count),
            median_friend_count = median(friend_count),
            n=n()) %>%
  ungroup() %>%
  arrange(age)

ggplot(aes(x=age,y=mean_friend_count),
       data=pf.fc_by_age_gender) +
  geom_line(aes(color = gender))

pf.fc_by_age_gender.wide <- dcast(pf.fc_by_age_gender,
                                  age ~ gender,
                                  value.var = 'median_friend_count')

head(pf.fc_by_age_gender.wide)
ggplot(aes(x=age, y = female/male),
       data = pf.fc_by_age_gender.wide) +
  geom_line() + 
  geom_hline(yintercept = 1, alpha = 0.3, linetype=2)

pf$year_joined = floor(2014-pf$tenure/365)
summary(pf$year_joined)
pf$year_joined.bucket <- cut(pf$year_joined,
                             c(2004, 2009, 2011, 2012, 2014))

ggplot(aes(x=age,y=friend_count),
       data=subset(pf, !is.na(year_joined.bucket))) +
  geom_line(aes(color = year_joined.bucket), stat = 'summary', fun.y = median)

ggplot(aes(x=age,y=friend_count),
       data=subset(pf, !is.na(year_joined.bucket))) +
  geom_line(aes(color = year_joined.bucket), 
            stat = 'summary', 
            fun.y = mean) +
  geom_line(stat = 'summary', fun.y=mean, linetype = 2)

with(subset(pf, tenure >= 1), summary(friend_count/tenure))

ggplot(aes(x=tenure, y=friendships_initiated/tenure),
       data=subset(pf, tenure >= 1)) +
  geom_line(aes(color=year_joined.bucket))

pf$prop_initiated = pf$friendships_initiated/pf$friend_count

ggplot(aes(y = prop_initiated, x=tenure), data = pf) + 
  geom_line(aes(color=year_joined.bucket),
            stat = 'summary',
            fun.y=median)

pf1<-subset(pf, year_joined.bucket='(2012,2014]')
summary(pf1$prop_initiated)

ggplot(aes(x = 7 * round(tenure / 7), y = friendships_initiated / tenure),
       data = subset(pf, tenure > 0)) +
  geom_line(aes(color = year_joined.bucket),
              stat = "summary",
              fun.y = mean)

ggplot(aes(x = 7 * round(tenure / 7), 
           y = friendships_initiated / tenure),
       data = subset(pf, tenure > 0)) +
  geom_smooth(aes(color = year_joined.bucket))

yo <- read.csv("./EDA_Course_Materials/yogurt.csv")
yo$id <- factor(yo$id)

ggplot(aes(price), data=yo)) +
  geom_histogram()
qplot(data=yo, x=price)

yo$all.purchases <- yo$pina.colada+yo$strawberry+
  yo$blueberry+yo$mixed.berry+yo$plain

ggplot(aes(x=time, y=price), data=yo) +
  geom_jitter(alpha=1/4, shape=21)

set.seed(4230)
sample.ids <- sample(levels(yo$id), 16)

ggplot(aes(x=time, y=price),
       data=subset(yo, id %in% sample.ids)) +
  facet_wrap(~ id) +
  geom_line() +
  geom_point(aes(size = all.purchases), pch = 1)

theme_set(theme_minimal(20))
set.seed(1836)
pf_subset <- pf[, c(2:15)]
names(pf_subset)
ggpairs(pf_subset[sample.int(nrow(pf_subset), 1000),])