package com.newsaggregator;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.clustering.Clusterer;
import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

import java.util.ArrayList;
import java.util.List;


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

            OutletArticle murrayBBC = new OutletArticle("Murray set to miss Davis Cup tie", "Andy Murray is unlikely to play in Great Britain's Davis Cup quarter-final against France next month as he recovers from an elbow injury. GB captain Leon Smith will name his team for the tie, in Rouen from 7-9 April, at 12:00 BST on Tuesday. Murray, 29, pulled out of the Miami Open almost a week before his opening match because of an elbow injury. The world number one needs to rest \"some sort of tear\", his brother Jamie told reporters in Florida. He said Andy told him he could \"do everything except serve\" and \"rest was all he had to do\". \"I am not planning that he is going to be there,\" he added. \"If he is, then obviously that's great for the team and we'll see what happens, but the most important thing for him is just to get healthy.\" Smith is likely to call on Dan Evans and Kyle Edmund for the singles matches in Rouen, with Jamie Murray and Dom Inglot in line for the doubles. The tie will be played indoors on clay at the Kindarena.", null, "http://wikipedia.org", "BBC", null);
            OutletArticle murrayGuardian = new OutletArticle("Murray set to miss Davis Cup tie", "The elbow injury Andy Murray suffered last week is set to rule the player out of Great Britain’s Davis Cup tie against France, according to his brother, Jamie. The quarter-final tie in Rouen, which starts a week on Friday, will apparently come much too early for the world No1 to make a recovery and he is not expected to be in the squad when it is revealed on Tuesday. “It’s some sort of tear in his elbow,” said Jamie Murray after his quarter-finals doubles match with partner Bruno Soares at the Miami Open on Sunday. “[Andy] said he can do everything except serve and he told me rest was all he had to do. I am not planning that he is going to be there [in Rouen to face France next week]. If he is, then, obviously that’s great for the team and we’ll see what happens, but the most important thing for him is just to get healthy because he has had a few issues now. “He has had shingles and he has had his elbow. He was sick here as well [with flu] for two or three days after he pulled out of the tournament. I think he just needs to get a bit settled and get a good crack at it over the next three months because there’s a lot of big tournaments to play.” Andy Murray had to withdraw from the Miami Open last week after being injured in practice and returned to Britain for scans on the elbow. The extent of the damage means Murray may struggle to be ready for the Monte Carlo Masters in mid-April, with more information about the injury likely to be revealed by his team in the coming days.", null, "http://wikipedia.org", "GuardianNew", null);
            OutletArticle murrayOtherArticle = new OutletArticle("Murray will take part in Davis Cup tie", "Andy Murray’s commitment to playing in Great Britain’s Davis Cup quarter-final against France in Rouen, which starts on 7 April, has been strengthened, oddly, by the injury that has forced his withdrawal from this week’s Miami Open. Murray’s plans for 2017 were always focused on remaining at the top of the rankings and a key part of his strategy depended on building momentum on the hard courts of Indian Wells and Miami, where he had the opportunity to put distance between him and his nearest rival, Novak Djokovic, after his own poor showing in the desert the previous season. However, his shock opening loss to Vasek Pospisil, the world No119, in Indian Wells, compounded by aggravation to a minor problem with his right elbow – coupled with Djokovic’s marginally less surprising defeat there by Nick Kyrgios – has forced a rethink. If Murray’s rehab goes well over the next two weeks, he will play in Rouen to test his elbow before the European clay court swing. As the rankings stand on Monday, Roger Federer has jumped four places to No6 on the back of his rousing victory over Stan Wawrinka in the Indian Wells final on Sunday. Murray has 12,005 points at the top of the table, followed by Djokovic on 8,915 and Wawrinka (5,705). Rounding out the top 10 are Kei Nishikori (4th: 4,730pts), Milos Raonic (5th: 4,480), Federer (6th: 4,305), Rafael Nadal (7th: 4,145), Dominic Thiem (8th: 3,465), Marin Cilic (9th: 3,420) and Jo-Wilfried Tsonga (10: 3,310). While Murray is naturally concerned about his dip in fortunes, the greater worry is with Djokovic. The Serb also withdrew from Miami – curiously, with an almost identical injury – giving up the 1,000 points he needed to defend as champion and gifting Murray further breathing space at the top of the rankings. Murray’s injury is recent, spotted during a warm-up in Indian Wells; Djokovic revealed via Facebook on Sunday that his problem reaches back to last summer, which will be of major concern to him going into the clay court season, where he has plenty of points to defend. The urgency to stay ahead of Djokovic has therefore eased slightly, but Murray, who is obsessive about his preparation, needs to finetune his clay court game before the Monte Carlo Open, which starts on 15 April. Djokovic is no certainty to play in Monte Carlo, where he lives. So, while Murray begins intensive rehabilitation on his elbow this week, he will think hard about testing it out in the clay of Rouen first. The world No1 missed the opening away tie against Canada, which Great Britain won 3-2, but is keen to be part of another campaign after the triumph over Belgium in the final in Ghent two years ago. The team captain, Leon Smith, will announce his lineup 10 days before the start of the tie, but will be anxious that his best player is fit to play. The player they are all watching most closely again, however, is Federer, whose revival at 35 after career-threatening knee surgery last year was crowned with his victory at the Australian Open and confirmed in Indian Wells. “It’s been just a fairytale week once again,” 35-year-old Federer said after winning his fifth title at a tournament he first visited 17 years ago. “I’m still on the comeback. I hope my body is going to allow me to keep on playing.” Wawrinka, who had a good tournament and played well in the final, said: “He always had an answer. I had a few little opportunities that I could have maybe done better, but it wasn’t enough.”", null, "http://wikipedia.org", "GuardianOld", null);
            OutletArticle india = new OutletArticle("India win", "India wrapped up a 2-1 series victory over Australia by winning the fourth and deciding Test in Dharamsala. The hosts needed 106 in their second innings to win and reached their target for the loss of two wickets. They had resumed on 19-0 and Murali Vijay went for eight while Cheteshwar Pujara was run out without scoring. However, KL Rahul's unbeaten 51 - his sixth half century of the series - and stand-in skipper Ajinkya Rahane's 38 not out eased them to victory. Rahul hit the winning runs as India won the 10th out of 13 Tests in a marathon home campaign which saw the top-ranked team beat New Zealand, England, Bangladesh and Australia. India's only defeat in those contests came against Australia when they were thrashed by 333 runs in the first Test of the series. The hosts bounced back with victory in the second and, after the third was drawn, won the fourth without injured captain Virat Kohli to beat the number two ranked Test team. \"This is our best series win so far,\" said Kohli. \"The maturity and responsibility shown by the boys was great to see. \"The changes we made to our fitness regime have paid off. It has been a team season. The responsibility has been shared.\" The fourth Test turned on day three when India bowled Australia out for 137 but visiting captain Steve Smith, who scored three centuries in the series, said he was \"proud of the way the guys have competed\". He added: \"It was a magnificent series, probably one of best I have been involved in. \"A lot of people wrote us off before we got here but I am proud of how the guys adapted. We probably let a few moments slip and against India you cannot do that.\"", null, "http://wikipedia.org", "BBC", null);
            ArrayList<OutletArticle> outletArticles = new ArrayList<OutletArticle>();
            outletArticles.add(murrayBBC);
            outletArticles.add(murrayGuardian);
            outletArticles.add(murrayOtherArticle);
            outletArticles.add(india);
            Clusterer clusterer = new Clusterer(outletArticles);
            List clusters = clusterer.cluster();
            System.out.println(clusters);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
