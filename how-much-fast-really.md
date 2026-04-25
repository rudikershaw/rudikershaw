## Forward

It's been quite some time since I wrote anything publicly. The last article I wrote was about how the usefulness of the tools and practices that software engineers use, is a question of sociology. In that article I explain that our industry doesn't do research of this kind very well, and that we should look to the complex sciences (medicine, psychology, sociology, economics, etc) for how to get answers to these questions. Questions like "Are AI Agents improving the speed that we write software"? This article expands on my last one, but this time we're going to talk about LLM agents. If you want to get the best out of this article, I encourage you to go read the last one. But before we begin, a quick detour.

## The Great Leap Forward

Roughly seventy years ago, under the leadership of Mao Zedong, the people of communist China were set the task of dramatically increasing agricultural output. Local officials, village leaders, and their supervisors were incentivised to report increases in output. Eager to impress, people all the way up the chain exaggerated those numbers. By the time the figures reached the top, the reported harvests bore almost no relation to the actual harvests. Acting on the strength of these reports, the state collected grain quotas proportional to the reported output, and began exporting the apparent surplus. There was no surplus. There was barely enough rice to feed the farmers who had grown it. The famine that followed is widely considered to be among the worst in modern history.

It's easy to image that these figures (or metrics) were not all lies. Perhaps they only exaggerated a little. Perhaps they didn't report rice by weight, but by number of bag. Perhaps the bags just got smaller. Or perhaps they weighed their bags of rice while wet, straight after washing. What happened during The Great Leap Forward may be one of the most devastating example of Goodhart's law. When a measure becomes a target, it ceases to be a good measure. Rice production was the measure; but the moment the state started tracking and rewarding it, the numbers became detached from their purpose. 

Are we, in the software industry, in the middle of our own version of this story? Our industry, and a few names in particular, is currently claiming that we are working hundreds of times faster^1,2. And there is great pressure on companies with a stake in AI to make these claims. These claims, or the figures used to support them, are now being used to justify business decisions that affect hundreds of thousands of people directly. And, possibly billions of people indirectly.

Are we really moving faster, or are we just reporting bigger harvests? I think we are moving faster. But how much faster? A few people have been trying to find out, and their effort deserve a much wider audience.

## Counting What Counts

What metrics are being measured by the most talked about research? Let's take a flash tour of some of the heavy hitters.

### Measuring Productivity in HTTP Servers

Probably the single most cited piece of research in this area is the 2023 paper by Peng, Kalliamvakou, Cihon, and Demirer called "The Impact of AI on Developer Productivity"^3. The authors work for Microsoft, Github, and the MIT Sloan School of Management. The design of this study is great, and the paper is very accessible. A simple Randomised Controlled Trial (RCT). Ninety five contractor developers were randomly assigned into two groups. One group was given co-pilot and the other was not. Both groups were asked to implement a HTTP server. To tell whether the contractors completed the task, 12 simple integration tests were run against their solution. If the tests all passed, their work was complete.

So, what did they find? They measured task completion between groups, and they measured the speed of completion. The headline result was that co-pilot use sped up the task by 55.8%. The result was significant (p = 0.0017, i.e. very unlikely the result was a fluke). This figure was plastered all over Microsoft and Github marketing material. But what about the task completion rate? We don't talk about that. No difference between the groups.

That's great right? We have a figure! A 50% reduction in task time means it takes half the time with AI. That means a 100% increase in output for developer tasks, right? Well, hold your horses. We have some problems to discuss.

The biggest problem is the obvious one. We don't measure developer productivity in HTTP Servers. Especially not ones that can be written in ≈2 hours on average. Especially not ones that only need 12 integration tests. Do you think you could check whether an HTTP Server was specification compliant in 12 tests? No, me neither. This paper makes no attempt to measure code quality. That's fine, but we need to know this before we can make any judgements about software we actually have to maintain. 

The last problem isn't specifically with this paper. Sponsorship Bias^2. This is the fancy term for the fact the companies with a financial incentive to produce favourable results consistently exaggerate research claims. There is some incredibly damning evidence that results published by "sponsors" are 30+% more likely to produce favourable results^5. For a more extreme example, consider the fact that reviews of the health effects of secondhand smoke funded by the tobacco industry were almost 90 times more likely to conclude that secondhand smoke was not harmful^6.

### Once More, but in the Field

Peng (from the Microsoft 2023 paper), along with five others, set out to measure developer productivity with field experiments^7. Instead of measuring time for a single isolated toy task, this paper attempts to measure productivity in the working environment. Three independent field experiments were run across Microsoft, Accenture, and an anonymous Fortune 100 company. The experiments monitored the work of almost 5,000 developers. An attempt to measure productivity where it counts.

Before I get into the study design and issues for this one, let's just skip straight to the results. They averaged the 3 experiments results, and got a 26% increase in tasks completed amongst developers using co-pilot. Given that co-pilot had at least an extra year of development, that's quite a drop from 2023 claim of 100% output (55% reduction in time). But that's not the worst of it.

This paper has some real problems. Let's go through them, in no particular order. Firstly, the paper claims to be randomised. Except opting into the group getting co-pilot was voluntary. The two groups being compared are not the same. We're comparing willing volunteers against everyone else. This is called _self_-selection bias. To try to fix this, they apply a number of statistical methods to try to account for this bias. But these are patches at best, and worse still, the researchers admit to knowing which group is which when doing the analysis. No blinding on the researcher side means that all their statistical analysis choices are made in full knowledge of what they _want to find_. Later in the paper, they make this even worse. They admit that, because of methodological problems, that they can't see the effect of co-pilot. Then, they admit to manually selecting a period in the experiment to use for the analysis so they can get a result. This is a **massive** red flag. There's a lot more, but I'll only cover two. The _tasks_ they reported getting completed at an increase of 26% were pull requests. Not actual units of work. Not even pull requests that got merged, or approved, or anything specific like that. Just... pull requests. Lastly, the increase in tasks completed was almost totally accounted for by the increase amongst junior developers. Senior developers saw no significant increase in task completion rate.

On the surface this paper has all the rigour of science. All the right words, and even the correct statistical methods. But the paper reads like the researchers were actively and desperately looking for a nice figure to report. A figure their sponsor would be happy with.

### Weighing What Matters

So far the Microsoft papers have measured productivity in quickly hacked together HTTP Servers, and pull requests of unknown value (and dubious scientific rigour). The HTTP Server paper isn't the only paper to check time for toy example completion. Two others repeated tests like this with similar results^8,9. Another paper, measured increases in Github commits when new co-pilot features were released^10. This is better than nothing. And honestly, the study quality is better than a lot of research discussing software engineering tools and practices. Study quality in our field is increasing as it attracts attention from other fields. But they're still not a meaningfully useful measurement. Putting your rice in smaller bags doesn't feed more people. We can't eat numbers.

Are there any other ways we measure productivity? Well, Microsoft, Github, Anthropic, and OpenAI have all published survey results that show how much faster developers _think_ they are working. But, as you're about to see, we have a good reason to doubt the improvements we believe we're seeing.

## The METR Study

In July 2025, METR (Model Evaluation and Threat Research), an independent research non-profit working on AI risk, published a study titled Measuring the Impact of Early-2025 AI on Experienced Open-Source Developer Productivity^11. This paper is _nearly_ perfect. Almost all of the problems that all the papers above suffer from are accounted for in their work.

METR paid senior engineers to work on real high impact open source project tasks. The projects selected, were selected because they had extremely high pull request merging standards and exceptionally low rates of bugs being introduced. This meant the research automatically inherited a quality bar. The tasks weren't proxies for real work (like pull requests or HTTP servers). The developers were asked to estimate the work in the pool, given a task, and then randomly assigned to using or not-using AI. Their screen was recorded for the duration of their work (and therefore timed). These developers works on 246 real issues, averaging two hours of work on each issue. After the research was complete, the developers were surveyed to find out how much faster they thought they were working using AI coding assistants.

So, what did they find? In retrospect, developers thought they were 20% faster using their coding agents. They were actually, on average, 19% **slower**. Not faster, _slower_.

As part of their study, METR shared the study design and surveyed economists, engineers, and ML experts to ask what kind of difference they thought the research would observe. Everyone thought they would see 35+% speed increases. But if they were paying attention, and reading between the lines, of the existing papers they needn't of been surprised. Both of the Microsoft papers show huge discrepancies in gains between junior and senior developers. The Field Experiments paper showed that on average senior engineers showed no significant improvements. And that was just the average. The range plots on the field experiments paper showed that many senior engineers were being slowed down. Long before this study, research had shown that LLMs perform significantly worse on very large code bases, and the open source projects picked for the tasks averaged 1M+ lines of code.

Earlier, I mentioned that the paper was _nearly_ perfect. Only 16 developers took part in the study, and they were all senior developers that were extremely familiar with the code bases that they were working on. The study design lined up very nicely with all the things we know LLMs are not very good at. But these are very minor criticisms. METR have a great and accessible [write up on their website](https://metr.org/blog/2025-07-10-early-2025-ai-experienced-os-dev-study/). Any criticism you might have is openly discussed, either in their blog or in the original paper. This is still the highest quality paper on this area by a very long shot. 

METR planned to produce a more comprehensive follow-up to this paper, with more and varied participants, using the latest AI tools. Unfortunately it doesn't look like we will get a full paper from them. [They are having trouble sourcing developers](https://metr.org/blog/2026-02-24-uplift-update/) who are willing to work without AI in 2026. And trying to continue without those developers risks introducing biases they won't be able to account for. 

So, it looks like we're either going to need lots of money to convince people to take part in research like this... or, a new study design.

## Stanford and SWEPR

The Stanford [Software Engineering Productivity Research](https://softwareengineeringproductivity.stanford.edu/) (SWEPR) group wants to take a very practical, reflective, and deeply clever approach to studying software engineer productivity with and without AI.

Traditional metrics (lines of code, story points, commit counts, DORA) don't accurately measure engineering productivity. If I generate 10,000 lines of code and submit a pull request, many of the earlier studies in this article will say that I have "performed meaningful work worth measuring". If the whole thing turns out to be wrong, and I slowly re-write everything over dozens or hundreds of code changes and pull requests, that shouldn't be considered more meaningful work. For the Microsoft Field study, all this activity would be considered evidence of AI gains. Even if I threw the whole lot away afterwards and it never got used. The SWERP group intends to produce a study which measures code value, while accounting for _rework_ and meaningless contributions.



### References

1. Shopify CEO Tobi Lutke - https://www.cnbc.com/2025/04/07/shopify-ceo-prove-ai-cant-do-jobs-before-asking-for-more-headcount.html

2. Anthropic CEO Dario Amodei - https://yourstory.com/ai-story/anthropic-ceo-dario-amodei-lauds-surging-ai-momentum-india

3. 2023 The Impact of AI on Developer Productivity: Evidence from GitHub Copilot https://arxiv.org/abs/2302.06590

4. Oxford Uni Catalogue of Bias - Sponsorship Bias https://catalogofbias.org/biases/industry-sponsorship-bias/

5. 2017 Industry sponsorship and research outcome https://www.cochrane.org/evidence/MR000033_industry-sponsorship-and-research-outcome

6. 1998 Why review articles on the health effects of passive smoking reach different conclusions https://pubmed.ncbi.nlm.nih.gov/9605902/

7. Cui, Zheyuan and Demirer, Mert and Jaffe, Sonia and Musolff, Leon and Peng, Sida and Salz, Tobias, The Effects of Generative AI on High-Skilled Work: Evidence from Three Field Experiments with Software Developers (August 20, 2025). Available at SSRN: https://ssrn.com/abstract=4945566 or http://dx.doi.org/10.2139/ssrn.4945566

8. Thomas Weber, Maximilian Brandmaier, Albrecht Schmidt, and Sven Mayer. Significant productivity gains through programming with large language models. Proc. ACM Hum.-Comput.
Interact., 8(EICS), June 2024. doi: 10.1145/3661145. URL https://doi.org/10.1145/
3661145.

9. Elise Paradis, Kate Grey, Quinn Madison, Daye Nam, Andrew Macvean, Vahid Meimand,
Nan Zhang, Ben Ferrari-Church, and Satish Chandra. How much does ai impact development
speed? an enterprise-based randomized controlled trial, 2024. URL https://arxiv.org/
abs/2410.12944.

10. Doron Yeverechyahu, Raveesh Mayya, and Gal Oestreicher-Singer. The impact of large
language models on open-source innovation: Evidence from github copilot, 2025. URL
https://ssrn.com/abstract=4684662.

11. J Becker, N Rush, E Barnes, D Rein. Measuring the Impact of Early-2025 AI on Experienced Open-Source Developer Productivity. arXiv 2507.09089 (2025). https://arxiv.org/abs/2507.09089