package com.newsaggregator;

import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8182);

        component.getDefaultHost().attach("/api", new RouterApplication());

        component.start();


        try {

            //CandidateLabel label = Wikipedia.getOutlinksAndCategories("Moose_Jaw_Civic_Centre");

            // System.out.println(label);

//            TaskService scheduleManager = new TaskService();

//            scheduleManager.scheduleAtFixedRate(new ArticleFetchRunnable(), 60L, 600L, TimeUnit.SECONDS);

//            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
////            Articles articles = new Articles(db);
////            articles.getAllTopics();
////
////            Table table = db.getTable("Articles");
////
////            OutletArticle article = new OutletArticle("A title", "The body of an article", "www.imageUrl.com", "www.sampleArticleUrl.com", Outlet.Guardian.getSourceString());
////
////            ObjectMapper mapper = new ObjectMapper();
////
////            System.out.println("Adding a new item...");
////            PutItemOutcome outcome = table.putItem(new Item()
////                    .withPrimaryKey("articleUrl", article.getArticleUrl())
////                    .withMap("info", article.createNonPrimaryHashMap()));
////
////            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
////            System.out.println("Fetching articles");
//            List<OutletArticle> articleList = new Reuters().getArticles();
//            List<String> strings = articleList.stream().map(OutletArticle::getBody).collect(Collectors.toList());
//            TfIdf tfidf = new TfIdf(strings);
//            tfidf.performTfIdf(strings.get(1), "Trump");
//            Articles articleManager = new Articles(db);
//            System.out.println("Sending articles");
//            articleManager.saveArticles(articleList);
//            System.out.println("Done");

//            MongoClient mongoClient = new MongoClient("localhost", 27017);
//
//            MongoDatabase db = mongoClient.getDatabase("NewsAggregator");
//
//            Articles articleCollection = new Articles(db);
////
//            List<OutletArticle> articles = articleCollection.getAllArticles();
//            System.out.println("Starting modelling");
//            TopicModelling modelling = new TopicModelling();
//            List<TopicLabel> topics = modelling.trainTopics(articles);
////            modelling.getModel(articleList.get(0).getBody());
//            System.out.println("Finished modelling");
//           Topics topicDB = new Topics(db);
//            List<TopicLabel> labels = topicDB.getAllTopics();
//            topicDB.saveTopics(topics);

//
//            List<LabelledArticle> labelledArticleList = new ArrayList<>();
//
//            Random rand = new Random();
//
//            for (int i = 0; i < 5; i++) {
//                System.out.println("Now on article " + i);
//                int index = rand.nextInt(articles.size());
//                Topic topic = modelling.getModel(articles.get(index));
//                List<String> topicLabel = TopicLabelling.generateTopicLabel(topic);
//                labelledArticleList.add(new LabelledArticle(topicLabel, articles.get(index)));
//            }
//
//            for (int i = 0; i < 5; i++) {
//                System.out.println("Article Title: " + labelledArticleList.get(i).getArticle().getTitle());
//                System.out.println("Article Body: " + labelledArticleList.get(i).getArticle().getBody());
//                System.out.println("");
//                System.out.print("Labels: ");
//                System.out.println(labelledArticleList.get(i).getLabels().toString());
//            }


////            TOI ap = new TOI();
////            List<OutletArticle> articles = new ArrayList<>(ap.getArticles());
////            for (OutletArticle article : articles) {
////                System.out.println(article.getBody());
////            }
////            articles.addAll(Guardian.getArticles());
////            articles.addAll(new Independent().getArticles());
////            List<WikipediaArticle> wikipediaArticleList = new ArrayList<>(Wikipedia.getArticles("Tim Cook"));
////            wikipediaArticleList.stream().forEach(wikipediaArticle -> System.out.println(wikipediaArticle.getTitle() + ": " + wikipediaArticle.getExtract()));
//            //TopicModelling.trainTopics(articles);

//            OutletArticle murrayBBC = new OutletArticle("Murray set to miss Davis Cup tie", "Andy Murray is unlikely to play in Great Britain's Davis Cup quarter-final against France next month as he recovers from an elbow injury. GB captain Leon Smith will name his team for the tie, in Rouen from 7-9 April, at 12:00 BST on Tuesday. Murray, 29, pulled out of the Miami Open almost a week before his opening match because of an elbow injury. The world number one needs to rest \"some sort of tear\", his brother Jamie told reporters in Florida. He said Andy told him he could \"do everything except serve\" and \"rest was all he had to do\". \"I am not planning that he is going to be there,\" he added. \"If he is, then obviously that's great for the team and we'll see what happens, but the most important thing for him is just to get healthy.\" Smith is likely to call on Dan Evans and Kyle Edmund for the singles matches in Rouen, with Jamie Murray and Dom Inglot in line for the doubles. The tie will be played indoors on clay at the Kindarena.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:35:22Z");
//            OutletArticle murrayGuardian = new OutletArticle("Murray set to miss Davis Cup tie", "The elbow injury Andy Murray suffered last week is set to rule the player out of Great Britain’s Davis Cup tie against France, according to his brother, Jamie. The quarter-final tie in Rouen, which starts a week on Friday, will apparently come much too early for the world No1 to make a recovery and he is not expected to be in the squad when it is revealed on Tuesday. “It’s some sort of tear in his elbow,” said Jamie Murray after his quarter-finals doubles match with partner Bruno Soares at the Miami Open on Sunday. “[Andy] said he can do everything except serve and he told me rest was all he had to do. I am not planning that he is going to be there [in Rouen to face France next week]. If he is, then, obviously that’s great for the team and we’ll see what happens, but the most important thing for him is just to get healthy because he has had a few issues now. “He has had shingles and he has had his elbow. He was sick here as well [with flu] for two or three days after he pulled out of the tournament. I think he just needs to get a bit settled and get a good crack at it over the next three months because there’s a lot of big tournaments to play.” Andy Murray had to withdraw from the Miami Open last week after being injured in practice and returned to Britain for scans on the elbow. The extent of the damage means Murray may struggle to be ready for the Monte Carlo Masters in mid-April, with more information about the injury likely to be revealed by his team in the coming days.", null, "http://wikipedia.org", "GuardianNew", "2017-03-28T11:12:22Z");
//            OutletArticle murrayOtherArticle = new OutletArticle("Murray will take part in Davis Cup tie", "Andy Murray’s commitment to playing in Great Britain’s Davis Cup quarter-final against France in Rouen, which starts on 7 April, has been strengthened, oddly, by the injury that has forced his withdrawal from this week’s Miami Open. Murray’s plans for 2017 were always focused on remaining at the top of the rankings and a key part of his strategy depended on building momentum on the hard courts of Indian Wells and Miami, where he had the opportunity to put distance between him and his nearest rival, Novak Djokovic, after his own poor showing in the desert the previous season. However, his shock opening loss to Vasek Pospisil, the world No119, in Indian Wells, compounded by aggravation to a minor problem with his right elbow – coupled with Djokovic’s marginally less surprising defeat there by Nick Kyrgios – has forced a rethink. If Murray’s rehab goes well over the next two weeks, he will play in Rouen to test his elbow before the European clay court swing. As the rankings stand on Monday, Roger Federer has jumped four places to No6 on the back of his rousing victory over Stan Wawrinka in the Indian Wells final on Sunday. Murray has 12,005 points at the top of the table, followed by Djokovic on 8,915 and Wawrinka (5,705). Rounding out the top 10 are Kei Nishikori (4th: 4,730pts), Milos Raonic (5th: 4,480), Federer (6th: 4,305), Rafael Nadal (7th: 4,145), Dominic Thiem (8th: 3,465), Marin Cilic (9th: 3,420) and Jo-Wilfried Tsonga (10: 3,310). While Murray is naturally concerned about his dip in fortunes, the greater worry is with Djokovic. The Serb also withdrew from Miami – curiously, with an almost identical injury – giving up the 1,000 points he needed to defend as champion and gifting Murray further breathing space at the top of the rankings. Murray’s injury is recent, spotted during a warm-up in Indian Wells; Djokovic revealed via Facebook on Sunday that his problem reaches back to last summer, which will be of major concern to him going into the clay court season, where he has plenty of points to defend. The urgency to stay ahead of Djokovic has therefore eased slightly, but Murray, who is obsessive about his preparation, needs to finetune his clay court game before the Monte Carlo Open, which starts on 15 April. Djokovic is no certainty to play in Monte Carlo, where he lives. So, while Murray begins intensive rehabilitation on his elbow this week, he will think hard about testing it out in the clay of Rouen first. The world No1 missed the opening away tie against Canada, which Great Britain won 3-2, but is keen to be part of another campaign after the triumph over Belgium in the final in Ghent two years ago. The team captain, Leon Smith, will announce his lineup 10 days before the start of the tie, but will be anxious that his best player is fit to play. The player they are all watching most closely again, however, is Federer, whose revival at 35 after career-threatening knee surgery last year was crowned with his victory at the Australian Open and confirmed in Indian Wells. “It’s been just a fairytale week once again,” 35-year-old Federer said after winning his fifth title at a tournament he first visited 17 years ago. “I’m still on the comeback. I hope my body is going to allow me to keep on playing.” Wawrinka, who had a good tournament and played well in the final, said: “He always had an answer. I had a few little opportunities that I could have maybe done better, but it wasn’t enough.”", null, "http://wikipedia.org", "GuardianOld", "2017-03-26T11:35:22Z");
//            OutletArticle indiaBBC = new OutletArticle("India win", "India wrapped up a 2-1 series victory over Australia by winning the fourth and deciding Test in Dharamsala. The hosts needed 106 in their second innings to win and reached their target for the loss of two wickets. They had resumed on 19-0 and Murali Vijay went for eight while Cheteshwar Pujara was run out without scoring. However, KL Rahul's unbeaten 51 - his sixth half century of the series - and stand-in skipper Ajinkya Rahane's 38 not out eased them to victory. Rahul hit the winning runs as India won the 10th out of 13 Tests in a marathon home campaign which saw the top-ranked team beat New Zealand, England, Bangladesh and Australia. India's only defeat in those contests came against Australia when they were thrashed by 333 runs in the first Test of the series. The hosts bounced back with victory in the second and, after the third was drawn, won the fourth without injured captain Virat Kohli to beat the number two ranked Test team. \"This is our best series win so far,\" said Kohli. \"The maturity and responsibility shown by the boys was great to see. \"The changes we made to our fitness regime have paid off. It has been a team season. The responsibility has been shared.\" The fourth Test turned on day three when India bowled Australia out for 137 but visiting captain Steve Smith, who scored three centuries in the series, said he was \"proud of the way the guys have competed\". He added: \"It was a magnificent series, probably one of best I have been involved in. \"A lot of people wrote us off before we got here but I am proud of how the guys adapted. We probably let a few moments slip and against India you cannot do that.\"", null, "http://wikipedia.org", "BBC", "2017-03-28T11:36:22Z");
//            OutletArticle indiaGuardian = new OutletArticle("India win", "A series that banged and crashed its way around India through a month of pulsating struggle drifted away in less than a session as India cruised to an eight-wicket victory on the fourth morning in Dharamsala, reclaiming the Border-Gavaskar Trophy in the process. Australia’s meek collapse the previous afternoon ensured there would be no grandstand finish, the hosts left 106 for victory on a surface that was always going to stay true. Returning with 87 more to get, the hosts did the rest in 18 overs. KL Rahul was not out when the triumph came, giving him 403 series runs, alongside his acting captain Ajinkya Ranahe who finished with a dashing 38. Josh Hazlewood could do little more to force an incision to replicate the rot India’s seamers had started the day before, false strokes earned from the outset. But as is so often is the case when the result looks a formality, with so few runs to play with, nothing went his way. Curiously, Steve O’Keefe was given the first opportunity to open the attack at the other end, to negligible effect. When danger man Pat Cummins was given the ball it took him just seven deliveries to find Murali Vijay’s outside edge, pouched safely behind the wicket by Matthew Wade. When Glenn Maxwell, racing in to gather the ball and hit middle stump out of the ground in one smooth motion to leave Cheteshwar Puraja well short of his ground to end that same over, two wickets had fallen on 46. If something was brewing – a final twist in a series full of them – Rahane was having none of it. The new man immediately took on Cummins with a glorious cover drive then a powerful hook shot next ball. Two overs later, it was consecutive sixes, slaying Cummins over midwicket then somehow taking another short ball over the rope at cover. From there it was a stroll and Rahul raised his half-century when clipping O’Keefe through midwicket for the winning runs. Steve Smith’s men leave rightly proud of what has been achieved, but that’s cold comfort in the aftermath of an opportunity burned in this deciding rubber. The ample confidence they carried into the encounter was earned in the gutsy draw in Ranchi to keep the series alive. Upon their arrival, the pitch looked antipodean not subcontinental. Then Virat Kohli was ruled out. Then Smith won the toss. Their favouritism, however slight, was warranted. Yet it was the pattern of the series, from Bangalore onwards, that Australia weren’t able to successfully bank their gains. The middle-order collapse on the opening afternoon came after Smith and David Warner pushed Australia to an imposing 144-1. The second innings debacle immediately followed a stoic comeback with the ball to end India’s first innings with a manageable deficit of 32. In both Bangalore and Ranchi there were chances to shut Kohli’s men out from imposing positions, but none were taken. An overreliance on Smith is a logical point of criticism, Australia’s relatively inexperienced and collapse-prone batting line up unable to deliver on a consistent enough basis. Not least the vice-captain Warner, who tallied just 193 runs at an average of 24. The skipper will take personal satisfaction from the fact that he could have done little more. Hi 499 series runs, including three centuries on three considerably different surfaces and circumstances, reinforced his standing as the world’s best player. “It was a magnificent series and one of the best I’ve been a part of,” Smith said after play. “We’ve learnt a lot out of this series as a young side and we will take plenty out of this and it should hold us in good stead. I’m really proud of the way the boys have competed.” India’s come-from-behind win defied the fact that their own captain, Kohli, never fired a shot. But the supporting cast of Pujara and Rahul were able to time and again do the heavy lifting. With the ball, Ravindra Jadeja’s relentless spin netted 25 wickets at 19 apiece, ably supported by Ravichandran Ashwin, who was not at his most potent but still claimed 21 wickets along the way. They will also be encouraged by Umesh Yadav, a seamer with pace and swing who will serve as an excellent attack leader in Australian conditions next time around; essential if they are to buck the trend of these two nations and retain the trophy away from home when India visit next in November 2018. The lopsided final result doesn’t do justice to the nature of this riveting series. Few will forget the relentless tension, which made it one of the most watchable in modern memory. For Australia, it’s clear they were not quite ready to win here, but the progress made since their previous visit to the subcontinent, and from the Hobart debacle in November, is not for nothing either. For that, they now command respect. Next stop: the Ashes.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:32:22Z");
//            OutletArticle nzMinisterBBC = new OutletArticle("NZ Minister fist fight", "New Zealand's environment minister has been challenged to a fist fight by a conservationist over the government's \"swimmable rivers\" policy. Conservation trust manager Greg Byrnes posted an advert in the local paper calling on Dr Nick Smith to meet him for a boxing match at a swimming hole, the New Zealand Herald reports. Mr Byrnes says the spot is badly polluted, but still classed as suitable to swimming. In February, Dr Smith announced a new policy of making 90% of New Zealand's waterways safe for swimming by 2040, but it included changes to water quality standards. Critics say that means water previously considered not suitable for swimming would be labelled as safe under the new measures. The classified advert took a dig at that policy with its wording. \"The loser to frolic in the water hole for no less than 5 minutes,\" the advert reads. \"This is in line with my target to make 90% of all Members of the NZ Parliament believable by 2020.\" Mr Byrne says he isn't expecting an answer, but took out the ad to make a point. \"We've got a fantastic country, but we're fast-tracking it to not a nice place,\" he tells the paper. \"I can't imagine what the Canterbury plains will be like in 15 years, unless we do something.\" The likelihood of seeing any fisticuffs is slim, as a spokesman for the minister says he won't be responding to the challenge. In a statement Dr Smith said that the policy would require 1,000 km (620 miles) of waterways to be improved each year.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:35:58Z");
//            OutletArticle icelandMine = new OutletArticle("Iceland mine", "A historic mine in Iceland may have to be closed to the public because visitors keep pinching its crystals, environmental officials have warned. The Helgustadanama mine is famous for Icelandic spar, a type of transparent calcite which was historically used in scientific equipment. Tourist guides note that the area is protected and removing any spar is forbidden, but that message doesn't seem to be getting through to everyone. The Environment Agency says it has been asking for more government funding in order to patrol the area, but that so far hasn't been forthcoming. Olafur Arnar Jonsson from the agency tells public broadcaster RUV that if things don't change, the mine will have to be shut in order to protect what's left. Some locals are unhappy that funds have been spent improving access to the mine, including car parking and toilet facilities, but not on security measures to protect the site. Heidberg Hjelm, who lives nearby, says that some visitors come prepared with tools to chip away bits of spar and make off with it. The English-language Reykjavik Grapevine magazine is unimpressed with the news. \"Visitors keep stealing the crystals to keep as souvenirs and that's why we can't have nice things. Because people are terrible,\" journalist Nanna Arnadottir writes. Iceland is trying to manage a huge growth in tourist numbers: last year it saw 1.8 million visitors, a 39% increase on 2015. Tourism Minister Thordis Kolbrun R. Gylfadottir says the country doesn't want to increase the number of foreign travellers any further, and that one measure under consideration is limiting the number of people accessing a site at any one time.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:21:22Z");
//            ArrayList<OutletArticle> outletArticles = new ArrayList<OutletArticle>();
//            outletArticles.add(murrayBBC);
//            outletArticles.add(murrayGuardian);
//            outletArticles.add(murrayOtherArticle);
//            outletArticles.add(indiaBBC);
//            outletArticles.add(indiaGuardian);
//            outletArticles.add(nzMinisterBBC);
//            outletArticles.add(icelandMine);
//            List<OutletArticle> outletArticles = ArticleFetch.fetchArticles();
//            TopicModelling modelling = new TopicModelling();
//            List<TopicLabel> topics = modelling.trainTopics(outletArticles);
//            Map<String, List<OutletArticle>> articleMap = new HashMap<>();
//            int i = 0;
//            for (OutletArticle article : outletArticles) {
//                System.out.println("Now on article number " + i + "out of " + outletArticles.size());
//                if (!article.getBody().equals("")) {
//                    Topic topic = modelling.getModel(article);
//                    List<String> topicLabel = TopicLabelling.generateTopicLabel(topic, article);
//                    for (String label : topicLabel) {
//                        if (articleMap.containsKey(label)) {
//                            List<OutletArticle> articles = articleMap.get(label);
//                            articles.add(article);
//                        } else {
//                            List<OutletArticle> articles = new ArrayList<>();
//                            articles.add(article);
//                            articleMap.put(label, articles);
//                        }
//                    }
//                }
//                i++;
//            }
//            for (Map.Entry<String, List<OutletArticle>> entry : articleMap.entrySet()) {
//                Clusterer clusterer = new Clusterer(entry.getValue());
//                List<Cluster<ArticleVector>> clusters = clusterer.cluster();
//                System.out.println("Now doing clustering for label " + entry.getKey());
//                for (Cluster<ArticleVector> cluster : clusters) {
//                    System.out.println("New Cluster:");
//                    for (ArticleVector vector : cluster.getClusterItems()) {
//                        System.out.println(vector.getArticle().getTitle());
//                    }
//                    System.out.println("Cluster size: " + cluster.getClusterItems().size());
//                    System.out.println("");
//                }
//                System.out.println("Total number of clusters: " + clusters.size());
//                System.out.println("");
//            }

            Extractive extractive = new Extractive();
            ArrayList<String> articleTests = new ArrayList<>();
            articleTests.add("New Zealand's environment minister has been challenged to a fist fight by a conservationist over the government's \"swimmable rivers\" policy. Conservation trust manager Greg Byrnes posted an advert in the local paper calling on Dr Nick Smith to meet him for a boxing match at a swimming hole, the New Zealand Herald reports. Mr Byrnes says the spot is badly polluted, but still classed as suitable to swimming. In February, Dr Smith announced a new policy of making 90% of New Zealand's waterways safe for swimming by 2040, but it included changes to water quality standards. Critics say that means water previously considered not suitable for swimming would be labelled as safe under the new measures. The classified advert took a dig at that policy with its wording. \"The loser to frolic in the water hole for no less than 5 minutes,\" the advert reads. \"This is in line with my target to make 90% of all Members of the NZ Parliament believable by 2020.\" Mr Byrne says he isn't expecting an answer, but took out the ad to make a point. \"We've got a fantastic country, but we're fast-tracking it to not a nice place,\" he tells the paper. \"I can't imagine what the Canterbury plains will be like in 15 years, unless we do something.\" The likelihood of seeing any fisticuffs is slim, as a spokesman for the minister says he won't be responding to the challenge. In a statement Dr Smith said that the policy would require 1,000 km (620 miles) of waterways to be improved each year.");
            articleTests.add("A New Zealand environmentalist has challenged his country's environment minister to a fist fight.  Greg Byrnes, general manager of the Te Kohaka o Tuhaitara conservation trust, issued the challenge to Nick Smith in a newspaper classified ad. He asked the minister to meet him in Christchurch for a boxing match with \"Queensberry Rules.\" The loser has to \"frolic\" in a local swimming hole that Greg Byrnes says is no longer fit for swimming.");
            articleTests.add("A conservation trust manager angry at Environment Minister Nick Smith's swimmable river stance has challenged him to a boxing match - with the loser to \"frolic\" in one of Christchurch's most polluted swimming holes. Greg Byrnes posted a classified advert on Wednesday challenging Dr Smith to a \"Queensbury Rules fist fight\". He asked Dr Smith to meet him at the Otukaikino Swimming Hole, which he said was one of Christchurch's most polluted waterways, but was still officially called a swimming hole. \"The loser to frolic in the water hole for no less than 5 minutes. This is in line with my target to make 90% of all members of the NZ Parliament believable by 2020,\" he said in the advert. It was sparked by Dr Smith's new swimmable rivers policy, which aims to have 90 per cent of lakes and rivers swimmable by 2040 - but has lower standards for what is considered swimmable. Mr Byrnes is general manager of the Te Kōhaka o Tūhaitara conservation trust and a former Environment Canterbury planner, and said he saw the effect of pollution on Christchurch's waterways every day. Earlier this month Dr Smith took a dip in the Manawatu River, in front of gathered reporters, after the river was labelled the worst in the western world and an accord was signed to improve it. He did not expect Dr Smith to respond to his advert, but posted it because he believed the Government policy treated people \"like idiots\". \"Most people will hear that and think they're doing something, and they're not,\" he said.");
            String summary = extractive.summarise(articleTests);
            System.out.println(summary);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
