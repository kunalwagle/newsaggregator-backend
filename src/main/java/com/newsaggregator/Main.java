package com.newsaggregator;

import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;


public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();

        int port = Integer.parseInt(args[0]);

        component.getServers().add(Protocol.HTTP, port);

        component.getDefaultHost().attach("/api", new RouterApplication());

        component.start();

        System.setProperty("aws.accessKeyId", "AKIAJTQREEILP5BCRQAA");
        System.setProperty("aws.secretKey", "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");

        boolean serverInitialised = false;

        while (!serverInitialised) {

            try {

                //CandidateLabel label = Wikipedia.getOutlinksAndCategories("Moose_Jaw_Civic_Centre");

                // System.out.println(label);

//            throw new Exception();
//                new TopicModelling();

//                BBCNews bbcNews = new BBCNews();
//                List<OutletArticle> articles = bbcNews.getArticles();

//                TaskServiceSingleton.getInstance().execute(new TopicLabelRunnable(Lists.newArrayList(new Articles(Utils.getDatabase()).getSingleArticle("http://www.bbc.co.uk/sport/tennis/40003294"))));
//
//                System.out.println(articles);

//                new TopicModelling().trainTopics(new Articles(Utils.getDatabase()).getAllArticles());
//                System.out.println("done");

//                OutletArticle article = new Articles(Utils.getDatabase()).getSingleArticle("http://www.bbc.co.uk/sport/tennis/40003294");
////                ExtractSentenceTypes extractSentenceTypes = NLPSingleton.getInstance();
//
//                StanfordCoreNLP pipeline = new StanfordCoreNLP();
//                Annotation annotation = new Annotation(article.getBody());
//                pipeline.annotate(annotation);
//                StanfordAnalysis stanfordAnalysis = new StanfordAnalysis(article.getSource(), annotation);
//
//                StringBuilder sb = new StringBuilder();
//
//                List<String> tokens = new ArrayList<>();
//
//                for (CoreMap sentence : stanfordAnalysis.getSentences()) {
//                    // traversing the words in the current sentence, "O" is a sensible default to initialise
//                    // tokens to since we're not interested in unclassified / unknown things..
//                    String prevNeToken = "O";
//                    String currNeToken = "O";
//                    boolean newToken = true;
//                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                        currNeToken = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//                        String word = token.get(CoreAnnotations.TextAnnotation.class);
//                        // Strip out "O"s completely, makes code below easier to understand
//                        if (currNeToken.equals("O")) {
//                            // LOG.debug("Skipping '{}' classified as {}", word, currNeToken);
//                            if (!prevNeToken.equals("O") && (sb.length() > 0)) {
//                                tokens.add(sb.toString());
//                                sb.setLength(0);
//                                newToken = true;
//                            }
//                            continue;
//                        }
//
//                        if (newToken) {
//                            prevNeToken = currNeToken;
//                            newToken = false;
//                            sb.append(" " + word);
//                            continue;
//                        }
//
//                        if (currNeToken.equals(prevNeToken)) {
//                            sb.append(" " + word);
//                        } else {
//                            // We're done with the current entity - print it out and reset
//                            tokens.add(sb.toString());
//                            sb.setLength(0);
//                            sb.append(word);
//                            newToken = true;
//                        }
//                        prevNeToken = currNeToken;
//                    }
//                }
//
//                tokens = tokens.stream().map(String::trim).collect(Collectors.toList());
//
//
////                List<String> nounPhrases = extractSentenceTypes.nounPhrases(article.getBody());
//
//                System.out.println(tokens);

//                if (args[0].equals("8182")) {

//                TaskService scheduleManager = TaskServiceSingleton.getInstance();
//
////                    Utils.sendServerRestartEmail(port);
//
//                LocalDateTime localNow = LocalDateTime.now();
//                ZoneId currentZone = ZoneId.of("Europe/London");
//                ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
//                ZonedDateTime zonedNext5;
//                zonedNext5 = zonedNow.withHour(5).withMinute(0).withSecond(0);
//                if (zonedNow.compareTo(zonedNext5) > 0)
//                    zonedNext5 = zonedNext5.plusDays(1);
//
//                Duration duration = Duration.between(zonedNow, zonedNext5);
//                long initialDelay = duration.getSeconds();
//
//                Logger.getLogger(Main.class).info("The initial delay will be " + initialDelay);
//                scheduleManager.scheduleAtFixedRate(new ClusteringScheduleRunnable(), 3L, 15L, TimeUnit.MINUTES);
//                scheduleManager.scheduleAtFixedRate(new SummarisingScheduleRunnable(), 2L, 15L, TimeUnit.MINUTES);
//                scheduleManager.schedule(new SendEmailRunnable(), 1L, TimeUnit.MINUTES);
//                scheduleManager.schedule(new LabellingRunnable(), 1L, TimeUnit.MINUTES);
//                scheduleManager.schedule(new ArticleFetchRunnable(), 1L, TimeUnit.SECONDS);
//                scheduleManager.scheduleAtFixedRate(new DigestRunnable(), initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
//                }

//                HomeArticles homeArticles = new HomeArticles(Utils.getDatabase());
//
//                homeArticles.initialise();
//
//                System.out.println("Initialised");

//                TaskServiceSingleton.getInstance().execute(new ClusteringRunnable(Lists.newArrayList("James Comey"), new ArrayList<>(), false));

//                OutletArticle outletArticle = new OutletArticle("Introduction", "The onset of the digital age has resulted in an insurmountable volume of news for a consumer to read. With the advent of the internet, not only are all the traditional news outlets publishing stories (and not just about local news, but news from all over the globe), but blogs are getting in on the act as well. In May 2017 President Donald Trump visited the Vatican. If I were to search Google News on the 24th May 2017, Id find a story about the Presidents meeting with the Pope. On further inspection, I can see that there are several pages of results about the meeting itself, from dozens of different news sources. Theyve all covered the story from different perspectives as well. Some have covered the contents of the meeting itself, some covered the general concept of the meeting in the wider context of Trumps trip to Saudi Arabia earlier that week, and some even chose to focus on everyones attire at the meeting. When a consumer has to search for the news and find these sources themselves, its more than probable that they will end up missing something. Also, which sources should they go to? And how many sources are enough to get a full and accurate picture? News Aggregators, such as Flipboard and Apple News, remove the requirement for a user to source articles themselves. The aggregators find the news and present it to the user in a way that is nicely grouped by topic. Other aggregators, such as Google News, allow consumers to search for news topics of their choosing as well, and present all the articles it finds on the same topic. But these solutions dont go far enough, as they dont address a major issue - there is still a very high volume of news. In July 2016 a study was conducted by the Statistic Brain Research Institute that said that the average attention span of a person is now as low as 8.25 seconds. In addition to this, the Institutes findings also said that the average person only reads 28\\% of a webpage of average length (593 words). Another key finding was that users spend only 4.4 seconds to cover each additional 100 words on a webpage. Its simply not feasible for a consumer to go through several different sources take on a piece of news, which is what many aggregators still expect them to do. My solution is for a news aggregator that goes further. My news aggregator, NewSumm, would have a primary aim of not only aggregating news, but of combining multiple sources articles on a piece of news, and summarising them into one short piece. Using the example from before, the intention would be that the consumer only has to read one summary that incorporates the facts from several different sources on Trumps visit to the Vatican. There is also the issue of reliability and trust to consider. The Digital News Report 2016 found that in the United Kingdom only 50\\% of readers trust what they see in the news. Could this be a case that there is simply so much news that it is hard to tell which is real? This could be the case because some readers will also be aware that a consequence of the new digital age is that anyone with an internet connection can create a blog and start spreading fake news. Whilst it should be emphasised that the primary aim of NewSumm would be to reduce the sheer volume of news that a consumer has to read, it could be argued that the problem of fake news could be indirectly minimised in the same way that other news aggregators attempt to tackle the issue. This would be by limiting articles to trusted sources from across the political spectrum. This would be reputable news organisations, and articles from random blogs on the internet would not be considered. In addition to this, a clear indication when reading the summary of what facts different outlets all agree on, and what facts they either differ on or omit, will allow the user to easily corroborate parts of the story. A further secondary aim to this could be to allow a user to customise their summary, taking in information from the outlets of their choice (from the original trusted list). The advantage to this would potentially be that if a user decides that too many issues from one source are uncorroborated, they can remove them, and see only information from the other sources that have written about a topic. The disadvantage to this could be that if a user dismisses an entire section of the political spectrum as uncorroborated continuously, theyll pick up a political bias on the facts of the topic. Although, it could be said that the Digital News Report 2016 findings suggest that this is a feature that wont be used much. One of the more interesting findings in the report was that more than 60\\% (in the United Kingdom) were concerned about the potential impact of personalising their news feeds. They felt that it could lead to both missing information and missing challenging viewpoints. The remainder of this report will follow the development of this concept, as both a website and as an iOS application. The following sections will set out NewSumm to be an ambitious project that aims to automatically fetch, label, cluster and summarise articles without supervision. NewSumm will be outlined as a user-friendly application that allows consumers a large level of control over what they read, through topic subscriptions and the ability to alter sources for summaries, as well as providing a daily digest for users who want them. Whilst the ultimate goal of an abstractive summarisation tool in the back end will prove to be infeasible, NewSumm, after thorough evaluation, will be found to be an application that provides more than competent summarisation of articles and effective clustering that goes a large way to reducing the large volume of news that a consumer has to get through. Going side by side with this evaluation of the fundamentals of the system itself is a rigorous analysis of the summaries produced to establish whether the system can be affected by the various political biases of its sources. The root causes of any political bias will also be fully investigated, and the role of the neutral source in a summary will also be discussed.", null, "introduction.co.uk", "introduction", null);
//
//                Extractive extractive = new Extractive(Lists.newArrayList(outletArticle));
//                Summary summary = extractive.summarise();
//                System.out.println(summary.getText());

//                new Users(Utils.getDatabase()).purgeUsers();
//                System.out.println("Purged users");

//                scheduleManager.execute(new DigestRunnable());
//                scheduleManager.execute(new LabellingRunnable());

//                scheduleManager.execute(new ClusteringRunnable(Lists.newArrayList(new Topics(Utils.getDatabase()).getTopicById("5925559bacea8273ab02efbb").getLabel())));

//                scheduleManager.scheduleAtFixedRate(new ClusteringScheduleRunnable(), 3L, 15L, TimeUnit.MINUTES);
//                scheduleManager.scheduleAtFixedRate(new SummarisingScheduleRunnable(), 2L, 15L, TimeUnit.MINUTES);
//                scheduleManager.scheduleAtFixedRate(new SendEmailRunnable(), 1L, 15L, TimeUnit.MINUTES);
//                scheduleManager.scheduleAtFixedRate(new DigestRunnable(), initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);

//               TopicModelling topicModelling = new TopicModelling();
//                OutletArticle murrayBBC = new OutletArticle("Murray set to miss Davis Cup tie", "Andy Murray is set to miss Davis Cup tie. Andy Murray is unlikely to play in Great Britain's Davis Cup quarter-final against France next month as he recovers from an elbow injury. GB captain Leon Smith will name his team for the tie, in Rouen from 7-9 April, at 12:00 BST on Tuesday. Murray, 29, pulled out of the Miami Open almost a week before his opening match because of an elbow injury. The world number one needs to rest \"some sort of tear\", his brother Jamie told reporters in Florida. He said Andy told him he could \"do everything except serve\" and \"rest was all he had to do\". \"I am not planning that he is going to be there,\" he added. \"If he is, then obviously that's great for the team and we'll see what happens, but the most important thing for him is just to get healthy.\" Smith is likely to call on Dan Evans and Kyle Edmund for the singles matches in Rouen, with Jamie Murray and Dom Inglot in line for the doubles. The tie will be played indoors on clay at the Kindarena.", "http://www.hellomagazine.com/imagenes//celebrities/2016100433872/andy-murray-stalked-hotel-maid/0-175-236/murray-d.jpg", "http://telegraph.co.uk/fakemurray", "the-telegraph", "2017-03-28T11:35:22Z");
//               Topic topic = topicModelling.getModel(murrayBBC);
//                System.out.println("Reached here");//Modeller.getInstance();
//                scheduleManager.execute(new ArticleFetchRunnable());
//                scheduleManager.execute(new LabellingRunnable());
//                scheduleManager.execute(new ClusteringRunnable());
//                scheduleManager.execute(new SummarisationRunnable());
//                scheduleManager.execute(new SendEmailRunnable());
//                scheduleManager.execute(new DigestRunnable());

                serverInitialised = true;

//                List<String> articleUrls = Lists.newArrayList(
//                        "http://www.dailymail.co.uk/sport/football/article-4614042/Cristiano-Ronaldo-cost-350m-summer.html",
//                        "http://uk.businessinsider.com/cristiano-ronaldo-reportedly-wants-to-leave-real-madrid-2017-6",
//                        "http://timesofindia.indiatimes.com/sports/football/top-stories/cristiano-ronaldo-willing-to-leave-real-madrid-say-reports/articleshow/59182797.cms",
//                        "http://metro.co.uk/2017/06/16/cristiano-ronaldo-tells-real-madrid-president-he-wants-to-leave-this-summer-6712631/",
//                        "https://www.fourfourtwo.com/news/ronaldo-seems-very-serious-amid-real-madrid-exit-storm-says-silva",
//                        "https://www.washingtonpost.com/news/early-lead/wp/2017/06/16/will-christiano-ronaldo-leave-real-madrid-because-of-his-spanish-tax-problems/?utm_term=.a88c84cf61e1",
//                        "https://www.theguardian.com/football/2017/jun/16/cristiano-ronaldo-quit-real-madrid-spain-tax",
//                        "http://www.espn.co.uk/football/story/3144359/cristiano-ronaldo-wants-real-madrid-exit-amid-tax-accusations-source",
//                        "http://edition.cnn.com/2017/06/16/football/cristiano-ronaldo-real-madrid-rumours/index.html",
//                        "http://www.telegraph.co.uk/football/2017/06/16/cristiano-ronaldo-makes-irreversible-decision-leave-real-madrid/",
//                        "http://www.bbc.co.uk/sport/football/40302296"
//                );
//
//                List<OutletArticle> articles = Lists.newArrayList(
//                        new OutletArticle("Title", new DailyMail().getSingleArticle(articleUrls.get(0)), "", articleUrls.get(0), Outlet.DailyMail.getSourceString(), ""),
//                        new OutletArticle("Title", new BusinessInsiderUK().getSingleArticle(articleUrls.get(1)), "", articleUrls.get(1), Outlet.BusinessInsiderUK.getSourceString(), ""),
//                        new OutletArticle("Title", new TOI().getSingleArticle(articleUrls.get(2)), "", articleUrls.get(2), Outlet.TOI.getSourceString(), ""),
//                        new OutletArticle("Title", new Metro().getSingleArticle(articleUrls.get(3)), "", articleUrls.get(3), Outlet.Metro.getSourceString(), ""),
//                        new OutletArticle("Title", new FourFourTwo().getSingleArticle(articleUrls.get(4)), "", articleUrls.get(4), Outlet.FourFourTwo.getSourceString(), ""),
//                        new OutletArticle("Title", new WashingtonPost().getSingleArticle(articleUrls.get(5)), "", articleUrls.get(5), Outlet.WashingtonPost.getSourceString(), ""),
//                        new OutletArticle("Title", new ESPN().getSingleArticle(articleUrls.get(7)), "", articleUrls.get(7), Outlet.ESPN.getSourceString(), ""),
//                        new OutletArticle("Title", new CNN().getSingleArticle(articleUrls.get(8)), "", articleUrls.get(8), Outlet.CNN.getSourceString(), ""),
//                        new OutletArticle("Title", new Telegraph().getSingleArticle(articleUrls.get(9)), "", articleUrls.get(9), Outlet.Telegraph.getSourceString(), ""),
//                        new OutletArticle("Title", new BBCSport().getSingleArticle(articleUrls.get(10)), "", articleUrls.get(10), Outlet.BBCSport.getSourceString(), "")
//                );
//
//                Extractive extractive = new Extractive(articles);
//
//                Summary summary = extractive.summarise();
//
//                System.out.println(summary.getNodes().stream().map(Node::getSource).collect(Collectors.toList()).toString());

//                Topics topics = new Topics(Utils.getDatabase());
//                LabelHolder labelHolder = topics.getTopicById("58ff77a2acea826a39311a26");
//                List<OutletArticle> articles = labelHolder.getArticles();
//            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
//            Articles articles = new Articles(db);
//            List<OutletArticle> articleList = articles.getAllArticles();
//            articles.saveArticles(articleList);
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
////            System.out.println("PutItem succeeded:" + outcome.getPutItemResult());
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

//                Articles articles = new Articles(Utils.getDatabase());
//                List<OutletArticle> article = Lists.newArrayList(articles.getArticleFromId("591c2759acea8240c68fa08b"));
//
//                List<Extractive> extractives = article.stream().map(a -> new Extractive(article)).collect(Collectors.toList());
//                List<Summary> summaries = extractives.stream().map(Extractive::summarise).collect(Collectors.toList());
//
//                System.out.println("Reached here");

////            TOI ap = new TOI();
//            List<OutletArticle> articles = new ArrayList<>();
//////            for (OutletArticle article : articles) {
//////                System.out.println(article.getBody());
//////            }
//            articles.addAll(Guardian.getArticles());
//////            articles.addAll(new Independent().getArticles());
//////            List<WikipediaArticle> wikipediaArticleList = new ArrayList<>(Wikipedia.getArticles("Tim Cook"));
//////            wikipediaArticleList.stream().forEach(wikipediaArticle -> System.out.println(wikipediaArticle.getTitle() + ": " + wikipediaArticle.getExtract()));
//            TopicModelling topicModelling = new TopicModelling();
//                List<TopicLabel> topicLabels = topicModelling.trainTopics(articles);
//                OutletArticle flynnBBC = new OutletArticle("Flynn", "President Donald Trump's ex-national security adviser, Michael Flynn, wants immunity to testify on alleged Russian election meddling, his lawyer says. Robert Kelner says his client \"has a story to tell\", but needs to guard against \"unfair prosecution\". Congress is investigating the allegations, with one senator warning of Kremlin \"propaganda on steroids\". Mr Flynn was sacked in February after misleading the White House about his conversations with a Russian envoy. His links to Russia are being scrutinised by the FBI and the House and Senate Intelligence Committees, as part of wider investigations into claims Moscow sought to help Donald Trump win the US presidential election, and into contacts between Russia and members of President Trump's campaign team. \"General Flynn certainly has a story to tell, and he very much wants to tell it, should the circumstances permit,\" said his attorney, Robert Kelner, in a written statement. He said he would not comment on his discussions with congressional panels conducting the investigation. The lawyer said the media was awash with \"unfounded allegations, outrageous claims of treason, and vicious innuendo\". \"No reasonable person, who has the benefit of advice from counsel, would submit to questioning in such a highly politicized, witch-hunt environment without assurances against unfair prosecution,\" he said. Three other former Trump aides, former campaign chief Paul Manafort and former advisers Roger Stone and Carter Page, have offered to testify without requesting immunity. The Senate Intelligence Committee opened its hearing on Thursday with one member saying Moscow had sought to \"hijack\" the US election. Democrat Mark Warner said Russia may have used technology to spread disinformation, including fake news for voters in key states, such as Wisconsin, Michigan and Pennsylvania. \"This Russian 'propaganda on steroids' was designed to poison the national conversation in America,\" Senator Warner said. Panel chairman Richard Burr, a Republican, warned: \"We are all targets of a sophisticated and capable adversary.\"" +
//                        "Russian President Putin has again dismissed the US claims as \"nonsense\" Mr Burr also confirmed there had been \"conversations\" about interviewing Mr Flynn, but his appearance had not been confirmed. The Trump presidency has faced continued allegations that members of its team colluded with Russian officials during the election campaign. The president regularly dismisses the claims as \"fake news\" and Russia has also ridiculed the allegations. President Putin did so again on Thursday at an Arctic forum, describing them as \"nonsense\" and \"irresponsible\".", null, "http://www.reuters.com/article/us-usa-trump-flynn-idUSKBN15S0BR", "associated-press", "2017-03-28T11:32:22Z");
//            Topic model = topicModelling.getModel(flynnBBC);
//            List<String> labels = TopicLabelling.generateTopicLabel(model, flynnBBC);
//
//                System.out.println(labels.toString());


//                                OutletArticle flynnGuardian = new OutletArticle("Flynn sacked by Trump", "Donald Trump’s former national security adviser, Michael Flynn, has all but confirmed that he offered to testify before the FBI and congressional committees about potential links between the Trump campaign and Russia in exchange for immunity." +
//                        "Flynn, who was ousted from his post in February for misleading the White House about discussions he held with the Russian ambassador to the US, released a statement on Thursday through his lawyer declaring that he had a story to tell – but was first seeking “assurances against unfair prosecution”. The Wall Street Journal first reported on Flynn’s offer to cut a deal with the FBI and Senate and House intelligence committees. “General Flynn certainly has a story to tell, and he very much wants to tell it, should the circumstances permit,” Flynn’s counsel, Robert Kelner, wrote in the statement. Kelner declined to comment on the details of Flynn’s conversations with the federal and congressional officials who are conducting separate inquiries into Russian interference in the US election. But he noted Flynn was “the target of unsubstantiated public demands by Members of Congress and other political critics that he be criminally investigated”. “No reasonable person, who has the benefit of advice from counsel, would submit to questioning in such a highly politicized, witch hunt environment without assurances against unfair prosecution.” But contrary to the assertion by Flynn’s lawyers that discussions with the relevant committees were already underway, top aides on the House intelligence committee disputed the claim. Jack Langer, a spokesman for the House intelligence committee chairman Devin Nunes, said no such offer had been made by Flynn in exchange for immunity. A Democratic committee aide also said members of his party had not been presented with an offer from Flynn. A spokeswoman for the Department of Justice declined to comment. Flynn was forced to resign from the Trump administration earlier this year after it was revealed he lied to Mike Pence about the nature of his conversations with Sergei Kislyak, the Russian ambassador to the US. Flynn spoke with the ambassador on multiple occasions during the transition process. One of those discussions pertained to US sanctions against Russia and occurred on 29 December – the same day Barack Obama levied more sanctions against Russian officials as an act of retribution toward its government for meddling in the election. Flynn was also paid nearly $68,000 in fees and expenses by Russian-linked companies in 2015, the majority of which came from the Russian government-backed television network RT. Immunity is typically sought to avoid penalty for breaking the law. Flynn agreed with the characterization while discussing the partial immunity granted to an aide to Hillary Clinton amid the federal government’s investigation of the former secretary of state’s use of a private email server at the state department. Speaking to NBC’s Meet the Press in September, two months before the election, Flynn stated: “When you are given immunity, that means you have probably committed a crime.”", "https://i.guim.co.uk/img/media/eba28cd0c2efe417874cfb2a06db8f277bbe2035/0_177_3000_1801/master/3000.jpg?w=620&q=20&auto=format&usm=12&fit=max&dpr=2&s=191e7c0f40e8f151f4e7c15bbed5c39a", "https://www.theguardian.com/us-news/2017/feb/13/michael-flynn-resigns-quits-trump-national-security-adviser-russia", "the-guardian-uk", "2017-03-28T11:32:22Z");
//                OutletArticle flynnTelegraph = new OutletArticle("Donald Trump sacks Flynn", "Mike Flynn, President Donald Trump's former national security adviser, is in talks with the House and Senate intelligence committees about giving testimony in their investigations of potential ties between the Trump campaign and Russia, his lawyer said on Thursday." +
//                        "Mr Flynn, who was fired in February for failing to disclose talks with Russia's ambassador before Mr Trump took office, had sought immunity from the FBI and the Congressional committees in exchange for his testimony, the Wall Street Journal reported, citing officials with knowledge of the matter. The newspaper said he had so far found no takers. Robert Kelner, Mr Flynn's lawyer, confirmed his client had held discussions with the House and Senate intelligence committees, but he did not mention the FBI. He said no \"reasonable person\" with legal counsel would answer questions without assurances. \"General Flynn certainly has a story to tell, and he very much wants to tell it, should the circumstances permit,\" Mr Kelner said. \"The media are awash with unfounded allegations, outrageous claims of treason, and vicious innuendo directed against him. \"He is now the target of unsubstantiated public demands by members of Congress and other political critics that he be criminally investigated. \"No reasonable person, who has the benefit of advice from counsel, would submit to questioning in such a highly politicised, witch hunt environment without assurances against unfair prosecution.\" The House of Representatives panel denied the Journal report. \"Michael Flynn has not offered to testify to HPSCI in exchange for immunity,\" Jack Langer, the committee spokesman, said in a statement. The FBI declined to comment. The Senate committee did not immediately respond to a request for comment. The FBI and the House and Senate intelligence committees are investigating allegations that Russians hacked Democratic Party computers and publicly disclosed the information in a bid to tip the November presidential election in favour of Mr Trump. They are also looking into possible links between the Trump campaign and Russians. Independent Senator Angus King, a member of the Senate Intelligence Committee, told CNN he could not confirm the Journal report, but \"if that turns out to be the case, that's a significant development I believe because it indicates that he has something important to say.\" Leading one of the investigations is Devin Nunes, the House intelligence committee chairman.  The White House refused on Thursday to say whether it secretly fed intelligence reports to Mr Nunes, fuelling concerns about political interference in the investigation. Fending off the growing criticism, the administration invited lawmakers from both parties to view classified material it said relates to surveillance of the president's associates. The invitation came as The New York Times reported that two White House officials - including an aide whose job was recently saved by Mr Trump - secretly helped Mr Nunes examine intelligence information. Last week Mr Nunes revealed he had seen intelligence reports showing Mr Trump was the subject of \"incidental surveillance\" by US agencies, and refused to reveal his source. Meanwhile, the Senate intelligence committee held its own hearing on Thursday, during which it was told that Vladimir Putin personally ordered a campaign to steal the presidential election from Hillary Clinton. Democratic Senator Mark Warner, vice-chairman of the Senate committee, said: \"Vladimir Putin ordered a deliberate campaign carefully constructed to undermine our election. There were paid internet trolls working out of a facility in Russia. \"This Russian 'propaganda on steroids' was designed to poison the national conversation in America.\" On whether there were links between Russia and Mr Trump's campaign Mr Warner said: \"We are seeking to determine whether there is an actual fire, but there is a great, great deal of smoke.\" Mr Putin issued his most emphatic denial so far on Thursday, calling the accusations he meddled in the US election “nonsense, endless, and groundless”.", "http://www.telegraph.co.uk/content/dam/news/2017/03/31/TELEMMGLPICT000124719866-large_trans_NvBQzQNjv4BqqVzuuqpFlyLIwiB6NTmJwSua9tVknf3TYifq4HXIURY.jpeg", "http://www.telegraph.co.uk/news/2017/03/31/mike-flynn-certainly-has-story-tell-trumps-fired-national-security/", "the-telegraph", "2017-03-28T11:32:22Z");
//                OutletArticle flynnMail = new OutletArticle("Flynn is gone!", "President Donald Trump's former national security adviser Michael Flynn is reportedly willing to testify about the Trump campaign's ties to Russia in exchange for immunity from prosecution. Flynn, who resigned as Trump's security adviser after just 24 days, has sent the request to both the FBI and the House and Senate intelligence committees, the Wall Street Journal reports. The agencies have not yet taken Flynn up on his offer, according to officials with knowledge of the matter. 'General Flynn certainly has a story to tell, and he very much wants to tell it, should the circumstances permit,' said Flynn's attorney, Robert Kelner. " +
//                        "President Donald Trump's former national security adviser Michael Flynn is reportedly willing to testify about the Trump campaign's ties to Russia in exchange for immunity " +
//                        "Kelner said no 'reasonable person' with legal counsel would answer questions without assurances that he would not be prosecuted, given calls from some members of Congress that the retired lieutenant general should face criminal charges.  Flynn's ties to Russia have been scrutinized by the FBI and are under investigation by the House and Senate intelligence committees. Both committees are looking into Russia's meddling in the 2016 presidential election and any ties between Trump associates and the Kremlin. His decision comes after he told NBC's Chuck Todd last year that anyone who seeks immunity has 'probably committed a crime'. He made the comments in relation to former Hillary Clinton staffers being granted immunity as the FBI investigated her use of her server when she was secretary of state.  'The very last thing that John Podesta (Clinton's campaign chairman) just said is no individual too big to jail, that should include people like Hillary Clinton,' he said." +
//                        "'I mean, five people around her have had, have been given immunity, to include her former Chief of Staff. When you are given immunity, that means that you have probably committed a crime.' Flynn's resignation in February came after reports emerged that he misled White House staff on his interactions with Russia and discussed sanctions with the Russian ambassador prior to the inauguration. Since July, the FBI has been conducting a counterintelligence investigation into Russia's interference in the election and possible coordination with Trump associates. Flynn's resignation in February came after reports emerged that he misled White House staff on his interactions with Russia and discussed sanctions with the Russian ambassador. The Justice Department had warned the Trump administration about Flynn's contacts with Russia weeks before he was forced out. " +
//                        "Government officials supposedly informed the White House because they were concerned Flynn could be vulnerable to blackmail by the Kremlin. The top Democrat on the Senate intelligence committee said on Wednesday that the panel 'will get to the bottom' of Russia's interference in the 2016 election. Sen. Mark Warner, the Virginia Democrat and vice chairman, and Sen. Richard Burr, the committee's Republican chairman, addressed reporters ahead of their panel's first hearing on Russia. The stakes for the Senate investigation have been heightened given the disarray in the House investigation into Russia. Democrats have called on Rep. Devin Nunes, the Republican chair of the House committee, to recuse himself over his close relationship with the Trump White House." +
//                        "Burr says the Senate committee has contacted 20 individuals about sitting for interviews. Among them is Jared Kushner, the president's son-in-law and senior adviser, who has acknowledged meetings with Russians during the transition.  ", null, "http://www.dailymail.co.uk/news/article-4221958/Michael-Flynn-compromised-DOJ-warns-White-House.html", "daily-mail", "2017-03-28T11:32:22Z");
//

//                OutletArticle murrayGuardian = new OutletArticle("Murray will miss Davis Cup tie", "Andy Murray will miss the Davis Cup tie. The elbow injury Andy Murray suffered last week is set to rule the player out of Great Britain’s Davis Cup tie against France, according to his brother, Jamie. The quarter-final tie in Rouen, which starts a week on Friday, will apparently come much too early for the world No1 to make a recovery and he is not expected to be in the squad when it is revealed on Tuesday. “It’s some sort of tear in his elbow,” said Jamie Murray after his quarter-finals doubles match with partner Bruno Soares at the Miami Open on Sunday. “[Andy] said he can do everything except serve and he told me rest was all he had to do. I am not planning that he is going to be there [in Rouen to face France next week]. If he is, then, obviously that’s great for the team and we’ll see what happens, but the most important thing for him is just to get healthy because he has had a few issues now. “He has had shingles and he has had his elbow. He was sick here as well [with flu] for two or three days after he pulled out of the tournament. I think he just needs to get a bit settled and get a good crack at it over the next three months because there’s a lot of big tournaments to play.” Andy Murray had to withdraw from the Miami Open last week after being injured in practice and returned to Britain for scans on the elbow. The extent of the damage means Murray may struggle to be ready for the Monte Carlo Masters in mid-April, with more information about the injury likely to be revealed by his team in the coming days.", "http://ichef.bbci.co.uk/news/976/cpsprodpb/2807/production/_92574201_epa_andymurray.jpg", "http://theguardian.co.uk/fakemurray", "the-guardian-uk", "2017-03-28T11:12:22Z");
////
//                List<OutletArticle> articles = Lists.newArrayList(flynnGuardian, flynnMail, flynnTelegraph);
////
//                Topics topicsManager = new Topics(Utils.getDatabase());
//                Articles articleManager = new Articles(Utils.getDatabase());
//                Summaries summaryManager = new Summaries(Utils.getDatabase());
//////////
//////                List<OutletArticle> articles = Lists.newArrayList(articleManager.getSingleArticle("https://www.theguardian.com/us-news/2017/feb/13/michael-flynn-resigns-quits-trump-national-security-adviser-russia"), articleManager.getSingleArticle("http://www.dailymail.co.uk/news/article-4221958/Michael-Flynn-compromised-DOJ-warns-White-House.html"), articleManager.getSingleArticle("http://www.telegraph.co.uk/news/2017/03/31/mike-flynn-certainly-has-story-tell-trumps-fired-national-security/"));
//                List<ClusterHolder> clusterHolder = summaryManager.getUnsummarisedClusters();

//                List<LabelHolder> topics = topicsManager.getAllTopics();
//
//                int counter = 1;
//                for (LabelHolder topic : topics) {
//                    System.out.println("Starting for " + counter + " out of " + topics.size());
//                    List<OutletArticle> articles = topic.getArticles();
//                    List<OutletArticle> newArticles = new ArrayList<>();
//                    for (OutletArticle article : articles) {
//                        if (newArticles.stream().noneMatch(a -> a.getArticleUrl().equals(article.getArticleUrl()))) {
//                            newArticles.add(article);
//                        }
//                    }
//                    topic.setArticles(articles);
//                }
//
//                System.out.println("Reached here");

//////
////                LabelHolder labelHolder = new LabelHolder("Michael T. Flynn", articles, new ArrayList<>());
////////
////                topicsManager.saveTopics(Lists.newArrayList(labelHolder));
//////
////////                topicsManager.createBlankTopic("Test Andy Murray");
////                articleManager.saveArticles(articles);
//
//                ClusterHolder clusterHolder = summaryManager.getSingleCluster("591b6f9e5dc53f7fb120f77b");
////////
//                LabelHolder labelHolder = topicsManager.getTopicById("591b6eab5dc53f7fa02b788b");
//
//                labelHolder.setClusters(Lists.newArrayList(clusterHolder));
//
//                topicsManager.saveTopic(labelHolder);

//                Set<OutletArticle> clusterArticles = new HashSet<>(labelHolder.getArticles());
//                Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
//                List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
//                List<Summary> summaries = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
////
//                ClusterHolder clusterHolder = new ClusterHolder(labelHolder.getArticles(), summaries);
//////
//                summaryManager.saveSummaries(Lists.newArrayList(clusterHolder));
//                LabelHolder labelHolder = new LabelHolder("Michael T. Flynn", articles, Lists.newArrayList(clusterHolder));
////                LabelHolder labelHolder = topicsManager.getTopic("Test Andy Murray");
////                labelHolder.addCluster(clusterHolder);
//                topicsManager.saveTopics(Lists.newArrayList(labelHolder));

//                OutletArticle indiaBBC = new OutletArticle("India defeat Australia", "India wrapped up a 2-1 series victory over Australia by winning the fourth and deciding Test in Dharamsala. The hosts needed 106 in their second innings to win and reached their target for the loss of two wickets. They had resumed on 19-0 and Murali Vijay went for eight while Cheteshwar Pujara was run out without scoring. However, KL Rahul's unbeaten 51 - his sixth half century of the series - and stand-in skipper Ajinkya Rahane's 38 not out eased them to victory. Rahul hit the winning runs as India won the 10th out of 13 Tests in a marathon home campaign which saw the top-ranked team beat New Zealand, England, Bangladesh and Australia. India's only defeat in those contests came against Australia when they were thrashed by 333 runs in the first Test of the series. The hosts bounced back with victory in the second and, after the third was drawn, won the fourth without injured captain Virat Kohli to beat the number two ranked Test team. \"This is our best series win so far,\" said Kohli. \"The maturity and responsibility shown by the boys was great to see. \"The changes we made to our fitness regime have paid off. It has been a team season. The responsibility has been shared.\" The fourth Test turned on day three when India bowled Australia out for 137 but visiting captain Steve Smith, who scored three centuries in the series, said he was \"proud of the way the guys have competed\". He added: \"It was a magnificent series, probably one of best I have been involved in. \"A lot of people wrote us off before we got here but I am proud of how the guys adapted. We probably let a few moments slip and against India you cannot do that.\"", null, "http://wikipedia.org", "BBC", "2017-03-28T11:36:22Z");
//            OutletArticle indiaGuardian = new OutletArticle("India vanquish the Aussies", "A series that banged and crashed its way around India through a month of pulsating struggle drifted away in less than a session as India cruised to an eight-wicket victory on the fourth morning in Dharamsala, reclaiming the Border-Gavaskar Trophy in the process. Australia’s meek collapse the previous afternoon ensured there would be no grandstand finish, the hosts left 106 for victory on a surface that was always going to stay true. Returning with 87 more to get, the hosts did the rest in 18 overs. KL Rahul was not out when the triumph came, giving him 403 series runs, alongside his acting captain Ajinkya Ranahe who finished with a dashing 38. Josh Hazlewood could do little more to force an incision to replicate the rot India’s seamers had started the day before, false strokes earned from the outset. But as is so often is the case when the result looks a formality, with so few runs to play with, nothing went his way. Curiously, Steve O’Keefe was given the first opportunity to open the attack at the other end, to negligible effect. When danger man Pat Cummins was given the ball it took him just seven deliveries to find Murali Vijay’s outside edge, pouched safely behind the wicket by Matthew Wade. When Glenn Maxwell, racing in to gather the ball and hit middle stump out of the ground in one smooth motion to leave Cheteshwar Puraja well short of his ground to end that same over, two wickets had fallen on 46. If something was brewing – a final twist in a series full of them – Rahane was having none of it. The new man immediately took on Cummins with a glorious cover drive then a powerful hook shot next ball. Two overs later, it was consecutive sixes, slaying Cummins over midwicket then somehow taking another short ball over the rope at cover. From there it was a stroll and Rahul raised his half-century when clipping O’Keefe through midwicket for the winning runs. Steve Smith’s men leave rightly proud of what has been achieved, but that’s cold comfort in the aftermath of an opportunity burned in this deciding rubber. The ample confidence they carried into the encounter was earned in the gutsy draw in Ranchi to keep the series alive. Upon their arrival, the pitch looked antipodean not subcontinental. Then Virat Kohli was ruled out. Then Smith won the toss. Their favouritism, however slight, was warranted. Yet it was the pattern of the series, from Bangalore onwards, that Australia weren’t able to successfully bank their gains. The middle-order collapse on the opening afternoon came after Smith and David Warner pushed Australia to an imposing 144-1. The second innings debacle immediately followed a stoic comeback with the ball to end India’s first innings with a manageable deficit of 32. In both Bangalore and Ranchi there were chances to shut Kohli’s men out from imposing positions, but none were taken. An overreliance on Smith is a logical point of criticism, Australia’s relatively inexperienced and collapse-prone batting line up unable to deliver on a consistent enough basis. Not least the vice-captain Warner, who tallied just 193 runs at an average of 24. The skipper will take personal satisfaction from the fact that he could have done little more. Hi 499 series runs, including three centuries on three considerably different surfaces and circumstances, reinforced his standing as the world’s best player. “It was a magnificent series and one of the best I’ve been a part of,” Smith said after play. “We’ve learnt a lot out of this series as a young side and we will take plenty out of this and it should hold us in good stead. I’m really proud of the way the boys have competed.” India’s come-from-behind win defied the fact that their own captain, Kohli, never fired a shot. But the supporting cast of Pujara and Rahul were able to time and again do the heavy lifting. With the ball, Ravindra Jadeja’s relentless spin netted 25 wickets at 19 apiece, ably supported by Ravichandran Ashwin, who was not at his most potent but still claimed 21 wickets along the way. They will also be encouraged by Umesh Yadav, a seamer with pace and swing who will serve as an excellent attack leader in Australian conditions next time around; essential if they are to buck the trend of these two nations and retain the trophy away from home when India visit next in November 2018. The lopsided final result doesn’t do justice to the nature of this riveting series. Few will forget the relentless tension, which made it one of the most watchable in modern memory. For Australia, it’s clear they were not quite ready to win here, but the progress made since their previous visit to the subcontinent, and from the Hobart debacle in November, is not for nothing either. For that, they now command respect. Next stop: the Ashes.", null, "http://wikipedia.org", "Guardian", "2017-03-28T11:32:22Z");
//            OutletArticle indiaCricinfo = new OutletArticle("India take victory", "Ajinkya Rahane sent a 146kph bouncer from Pat Cummins flying into the crowd at midwicket and, next ball, slapped another short one over the cover fence while backing away. These were the blows that finally snuffed out the last of Australia's fight, and sealed once and for all India's victory in this most bewitching of Border-Gavaskar series. Australia entered the fourth morning with only the scantest of hopes, defending a mere 87 runs and needing 10 wickets. This did not mean that contest was over, as Josh Hazlewood and Pat Cummins fired the ball down with pace and venom after a night's refreshment. The loss of M Vijay and Cheteshwar Pujara - the latter run out by a brilliant throw from Glenn Maxwell - kept Australia hoping. But KL Rahul and Rahane responded with bold blows to settle the matter. The win in Dharamsala ended India's marathon home Test season with four series victories out of four, and also means that the team presently holds series honours over every other nation in the five-day game. Rahane's stand-in captaincy, in the absence of the injured Virat Kohli, had been vital to this achievement, so too the runs of Rahul, the pace of Umesh Yadav and the all-round contribution of Ravindra Jadeja. There will be great satisfaction derived also from the fact that Dharamsala offered conditions more familiar to the tourists. For that reason, among others, Steven Smith's team were left to ponder a string of missed opportunities after their vast opening win in Pune. There have been times in all three Tests since that the Australians have looked very much in control of proceedings, but they have been unable to stay on the mountaintop under pressure from an Indian side roused into action by the shock of that first-up hiding. As the ball continued to bounce and swerve when India resumed their pursuit of a modest target today, Australia's fielders must have wondered what might have been with another 100 or so runs to defend. Josh Hazlewood went up for a pair of vociferous lbw appeals against Vijay in the opening over, but on both occasions the opener got the merest of bat to ball before it struck the pad. At the other end Cummins sent a bouncer down the leg side that may have touched Vijay's gloves before being taken on the juggle by Matthew Wade. Certainly Ultra Edge indicated as much, but only Wade raised the most half-hearted of appeals. More straightforward was another edge in Cummins' next over, near enough to an action replay of Vijay's first-innings dismissal, which offered a glimmer of light for Australia. Genuine excitement followed when Pujara and Rahul hesitated fatally in taking a quick single to the right arm of Maxwell, who threw down middle stump to send Pujara on his way with 60 still needed. In those moments the Australians wondered briefly what might be possible, and the Indian viewing area tensed up like with so many teams chasing a pesky small target in the past. But Rahane and Rahul barely put a foot out of place in the overs that followed, accumulating steadily until Cummins elected to go around the wicket for the tourists' final effort. Rahane's riposte, the first impressively orthodox, the second more redolent of the forthcoming IPL, said much about India's admirable resilience in the face of a most unexpected challenge.", "http://animage.com", "http://wikipedia.org", "cricinfo", "2017-03-28T11:32:22Z");
//                List<OutletArticle> articles = Lists.newArrayList(indiaBBC, indiaGuardian, indiaCricinfo);
//                ClusterHolder clusterHolder = new ClusterHolder(articles);
//                System.out.println("reached here");
//                Set<OutletArticle> clusterArticles = new HashSet<>(articles);
//                Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
//                List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
//                List<Summary> summaries = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
////                Summary summaries = new Extractive(articles).summarise();
//                System.out.println(summaries);
                //            OutletArticle murrayOtherArticle = new OutletArticle("Murray will take part in Davis Cup tie", "Andy Murray’s commitment to playing in Great Britain’s Davis Cup quarter-final against France in Rouen, which starts on 7 April, has been strengthened, oddly, by the injury that has forced his withdrawal from this week’s Miami Open. Murray’s plans for 2017 were always focused on remaining at the top of the rankings and a key part of his strategy depended on building momentum on the hard courts of Indian Wells and Miami, where he had the opportunity to put distance between him and his nearest rival, Novak Djokovic, after his own poor showing in the desert the previous season. However, his shock opening loss to Vasek Pospisil, the world No119, in Indian Wells, compounded by aggravation to a minor problem with his right elbow – coupled with Djokovic’s marginally less surprising defeat there by Nick Kyrgios – has forced a rethink. If Murray’s rehab goes well over the next two weeks, he will play in Rouen to test his elbow before the European clay court swing. As the rankings stand on Monday, Roger Federer has jumped four places to No6 on the back of his rousing victory over Stan Wawrinka in the Indian Wells final on Sunday. Murray has 12,005 points at the top of the table, followed by Djokovic on 8,915 and Wawrinka (5,705). Rounding out the top 10 are Kei Nishikori (4th: 4,730pts), Milos Raonic (5th: 4,480), Federer (6th: 4,305), Rafael Nadal (7th: 4,145), Dominic Thiem (8th: 3,465), Marin Cilic (9th: 3,420) and Jo-Wilfried Tsonga (10: 3,310). While Murray is naturally concerned about his dip in fortunes, the greater worry is with Djokovic. The Serb also withdrew from Miami – curiously, with an almost identical injury – giving up the 1,000 points he needed to defend as champion and gifting Murray further breathing space at the top of the rankings. Murray’s injury is recent, spotted during a warm-up in Indian Wells; Djokovic revealed via Facebook on Sunday that his problem reaches back to last summer, which will be of major concern to him going into the clay court season, where he has plenty of points to defend. The urgency to stay ahead of Djokovic has therefore eased slightly, but Murray, who is obsessive about his preparation, needs to finetune his clay court game before the Monte Carlo Open, which starts on 15 April. Djokovic is no certainty to play in Monte Carlo, where he lives. So, while Murray begins intensive rehabilitation on his elbow this week, he will think hard about testing it out in the clay of Rouen first. The world No1 missed the opening away tie against Canada, which Great Britain won 3-2, but is keen to be part of another campaign after the triumph over Belgium in the final in Ghent two years ago. The team captain, Leon Smith, will announce his lineup 10 days before the start of the tie, but will be anxious that his best player is fit to play. The player they are all watching most closely again, however, is Federer, whose revival at 35 after career-threatening knee surgery last year was crowned with his victory at the Australian Open and confirmed in Indian Wells. “It’s been just a fairytale week once again,” 35-year-old Federer said after winning his fifth title at a tournament he first visited 17 years ago. “I’m still on the comeback. I hope my body is going to allow me to keep on playing.” Wawrinka, who had a good tournament and played well in the final, said: “He always had an answer. I had a few little opportunities that I could have maybe done better, but it wasn’t enough.”", null, "http://wikipedia.org", "GuardianOld", "2017-03-26T11:35:22Z");
                //
//            //            OutletArticle nzMinisterBBC = new OutletArticle("NZ Minister fist fight", "New Zealand's environment minister has been challenged to a fist fight by a conservationist over the government's \"swimmable rivers\" policy. Conservation trust manager Greg Byrnes posted an advert in the local paper calling on Dr Nick Smith to meet him for a boxing match at a swimming hole, the New Zealand Herald reports. Mr Byrnes says the spot is badly polluted, but still classed as suitable to swimming. In February, Dr Smith announced a new policy of making 90% of New Zealand's waterways safe for swimming by 2040, but it included changes to water quality standards. Critics say that means water previously considered not suitable for swimming would be labelled as safe under the new measures. The classified advert took a dig at that policy with its wording. \"The loser to frolic in the water hole for no less than 5 minutes,\" the advert reads. \"This is in line with my target to make 90% of all Members of the NZ Parliament believable by 2020.\" Mr Byrne says he isn't expecting an answer, but took out the ad to make a point. \"We've got a fantastic country, but we're fast-tracking it to not a nice place,\" he tells the paper. \"I can't imagine what the Canterbury plains will be like in 15 years, unless we do something.\" The likelihood of seeing any fisticuffs is slim, as a spokesman for the minister says he won't be responding to the challenge. In a statement Dr Smith said that the policy would require 1,000 km (620 miles) of waterways to be improved each year.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:35:58Z");
////            OutletArticle icelandMine = new OutletArticle("Iceland mine", "A historic mine in Iceland may have to be closed to the public because visitors keep pinching its crystals, environmental officials have warned. The Helgustadanama mine is famous for Icelandic spar, a type of transparent calcite which was historically used in scientific equipment. Tourist guides note that the area is protected and removing any spar is forbidden, but that message doesn't seem to be getting through to everyone. The Environment Agency says it has been asking for more government funding in order to patrol the area, but that so far hasn't been forthcoming. Olafur Arnar Jonsson from the agency tells public broadcaster RUV that if things don't change, the mine will have to be shut in order to protect what's left. Some locals are unhappy that funds have been spent improving access to the mine, including car parking and toilet facilities, but not on security measures to protect the site. Heidberg Hjelm, who lives nearby, says that some visitors come prepared with tools to chip away bits of spar and make off with it. The English-language Reykjavik Grapevine magazine is unimpressed with the news. \"Visitors keep stealing the crystals to keep as souvenirs and that's why we can't have nice things. Because people are terrible,\" journalist Nanna Arnadottir writes. Iceland is trying to manage a huge growth in tourist numbers: last year it saw 1.8 million visitors, a 39% increase on 2015. Tourism Minister Thordis Kolbrun R. Gylfadottir says the country doesn't want to increase the number of foreign travellers any further, and that one measure under consideration is limiting the number of people accessing a site at any one time.", null, "http://wikipedia.org", "BBC", "2017-03-28T11:21:22Z");
//            ArrayList<OutletArticle> outletArticles = new ArrayList<>();
//            outletArticles.add(murrayBBC);
//            outletArticles.add(murrayGuardian);
////            outletArticles.add(murrayOtherArticle);
//            outletArticles.add(flynnBBC);
//            outletArticles.add(flynnGuardian);
//            outletArticles.add(flynnTelegraph);
//            outletArticles.add(flynnMail);
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
//                System.out.println("Starting Clustering");
//                Clusterer clusterer = new Clusterer(articles);
//                System.out.println("Starting Actual Clustering");
//                List<Cluster<ArticleVector>> clusters = clusterer.cluster();
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

//
//            Extractive extractive = new Extractive(outletArticles);
//////            ArrayList<String> articleTests = new ArrayList<>();
//////            articleTests.add("India wrapped up a 2-1 series victory over Australia by winning the fourth and deciding Test in Dharamsala. The hosts needed 106 in their second innings to win and reached their target for the loss of two wickets. They had resumed on 19-0 and Murali Vijay went for eight while Cheteshwar Pujara was run out without scoring. However, KL Rahul's unbeaten 51 - his sixth half century of the series - and stand-in skipper Ajinkya Rahane's 38 not out eased them to victory. Rahul hit the winning runs as India won the 10th out of 13 Tests in a marathon home campaign which saw the top-ranked team beat New Zealand, England, Bangladesh and Australia. India's only defeat in those contests came against Australia when they were thrashed by 333 runs in the first Test of the series. The hosts bounced back with victory in the second and, after the third was drawn, won the fourth without injured captain Virat Kohli to beat the number two ranked Test team.  \"This is our best series win so far,\" said Kohli. \"The maturity and responsibility shown by the boys was great to see. \"The changes we made to our fitness regime have paid off. It has been a team season. The responsibility has been shared.\" The fourth Test turned on day three when India bowled Australia out for 137 but visiting captain Steve Smith, who scored three centuries in the series, said he was \"proud of the way the guys have competed\". He added: \"It was a magnificent series, probably one of best I have been involved in. \"A lot of people wrote us off before we got here but I am proud of how the guys adapted. We probably let a few moments slip and against India you cannot do that.\"");
//////            articleTests.add("Ajinkya Rahane sent a 146kph bouncer from Pat Cummins flying into the crowd at midwicket and, next ball, slapped another short one over the cover fence while backing away. These were the blows that finally snuffed out the last of Australia's fight, and sealed once and for all India's victory in this most bewitching of Border-Gavaskar series. Australia entered the fourth morning with only the scantest of hopes, defending a mere 87 runs and needing 10 wickets. This did not mean that contest was over, as Josh Hazlewood and Pat Cummins fired the ball down with pace and venom after a night's refreshment. The loss of M Vijay and Cheteshwar Pujara - the latter run out by a brilliant throw from Glenn Maxwell - kept Australia hoping. But KL Rahul and Rahane responded with bold blows to settle the matter. The win in Dharamsala ended India's marathon home Test season with four series victories out of four, and also means that the team presently holds series honours over every other nation in the five-day game. Rahane's stand-in captaincy, in the absence of the injured Virat Kohli, had been vital to this achievement, so too the runs of Rahul, the pace of Umesh Yadav and the all-round contribution of Ravindra Jadeja. There will be great satisfaction derived also from the fact that Dharamsala offered conditions more familiar to the tourists. For that reason, among others, Steven Smith's team were left to ponder a string of missed opportunities after their vast opening win in Pune. There have been times in all three Tests since that the Australians have looked very much in control of proceedings, but they have been unable to stay on the mountaintop under pressure from an Indian side roused into action by the shock of that first-up hiding. As the ball continued to bounce and swerve when India resumed their pursuit of a modest target today, Australia's fielders must have wondered what might have been with another 100 or so runs to defend. Josh Hazlewood went up for a pair of vociferous lbw appeals against Vijay in the opening over, but on both occasions the opener got the merest of bat to ball before it struck the pad. At the other end Cummins sent a bouncer down the leg side that may have touched Vijay's gloves before being taken on the juggle by Matthew Wade. Certainly Ultra Edge indicated as much, but only Wade raised the most half-hearted of appeals. More straightforward was another edge in Cummins' next over, near enough to an action replay of Vijay's first-innings dismissal, which offered a glimmer of light for Australia. Genuine excitement followed when Pujara and Rahul hesitated fatally in taking a quick single to the right arm of Maxwell, who threw down middle stump to send Pujara on his way with 60 still needed. In those moments the Australians wondered briefly what might be possible, and the Indian viewing area tensed up like with so many teams chasing a pesky small target in the past. But Rahane and Rahul barely put a foot out of place in the overs that followed, accumulating steadily until Cummins elected to go around the wicket for the tourists' final effort. Rahane's riposte, the first impressively orthodox, the second more redolent of the forthcoming IPL, said much about India's admirable resilience in the face of a most unexpected challenge.");
//////            articleTests.add("A series that banged and crashed its way around India through a month of pulsating struggle drifted away in less than a session as India cruised to an eight-wicket victory on the fourth morning in Dharamsala, reclaiming the Border-Gavaskar Trophy in the process. Australia’s meek collapse the previous afternoon ensured there would be no grandstand finish, the hosts left 106 for victory on a surface that was always going to stay true. Returning with 87 more to get, the hosts did the rest in 18 overs. KL Rahul was not out when the triumph came, giving him 403 series runs, alongside his acting captain Ajinkya Ranahe who finished with a dashing 38. Josh Hazlewood could do little more to force an incision to replicate the rot India’s seamers had started the day before, false strokes earned from the outset. But as is so often is the case when the result looks a formality, with so few runs to play with, nothing went his way. Curiously, Steve O’Keefe was given the first opportunity to open the attack at the other end, to negligible effect. When danger man Pat Cummins was given the ball it took him just seven deliveries to find Murali Vijay’s outside edge, pouched safely behind the wicket by Matthew Wade. When Glenn Maxwell, racing in to gather the ball and hit middle stump out of the ground in one smooth motion to leave Cheteshwar Puraja well short of his ground to end that same over, two wickets had fallen on 46. If something was brewing – a final twist in a series full of them – Rahane was having none of it. The new man immediately took on Cummins with a glorious cover drive then a powerful hook shot next ball. Two overs later, it was consecutive sixes, slaying Cummins over midwicket then somehow taking another short ball over the rope at cover. From there it was a stroll and Rahul raised his half-century when clipping O’Keefe through midwicket for the winning runs. Steve Smith’s men leave rightly proud of what has been achieved, but that’s cold comfort in the aftermath of an opportunity burned in this deciding rubber. The ample confidence they carried into the encounter was earned in the gutsy draw in Ranchi to keep the series alive. Upon their arrival, the pitch looked antipodean not subcontinental. Then Virat Kohli was ruled out. Then Smith won the toss. Their favouritism, however slight, was warranted. Yet it was the pattern of the series, from Bangalore onwards, that Australia weren’t able to successfully bank their gains. The middle-order collapse on the opening afternoon came after Smith and David Warner pushed Australia to an imposing 144-1. The second innings debacle immediately followed a stoic comeback with the ball to end India’s first innings with a manageable deficit of 32. In both Bangalore and Ranchi there were chances to shut Kohli’s men out from imposing positions, but none were taken. An overreliance on Smith is a logical point of criticism, Australia’s relatively inexperienced and collapse-prone batting line up unable to deliver on a consistent enough basis. Not least the vice-captain Warner, who tallied just 193 runs at an average of 24. The skipper will take personal satisfaction from the fact that he could have done little more. Hi 499 series runs, including three centuries on three considerably different surfaces and circumstances, reinforced his standing as the world’s best player. “It was a magnificent series and one of the best I’ve been a part of,” Smith said after play. “We’ve learnt a lot out of this series as a young side and we will take plenty out of this and it should hold us in good stead. I’m really proud of the way the boys have competed.” India’s come-from-behind win defied the fact that their own captain, Kohli, never fired a shot. But the supporting cast of Pujara and Rahul were able to time and again do the heavy lifting. With the ball, Ravindra Jadeja’s relentless spin netted 25 wickets at 19 apiece, ably supported by Ravichandran Ashwin, who was not at his most potent but still claimed 21 wickets along the way. They will also be encouraged by Umesh Yadav, a seamer with pace and swing who will serve as an excellent attack leader in Australian conditions next time around; essential if they are to buck the trend of these two nations and retain the trophy away from home when India visit next in November 2018. The lopsided final result doesn’t do justice to the nature of this riveting series. Few will forget the relentless tension, which made it one of the most watchable in modern memory. For Australia, it’s clear they were not quite ready to win here, but the progress made since their previous visit to the subcontinent, and from the Hobart debacle in November, is not for nothing either. For that, they now command respect. Next stop: the Ashes.");
//            Summary summary = extractive.summarise();
//            System.out.println(summary.getText());
//            summary = new Abstractive(summary).summarise();
//            System.out.println(summary.getText());

//            StanfordNLP stanfordNLP = new StanfordNLP(murrayBBC);
//            StanfordAnalysis stanfordAnalysis = stanfordNLP.performAnalysis();
//            for(CoreMap sentence: stanfordAnalysis.getSentences()) {
//                // traversing the words in the current sentence
//                // a CoreLabel is a CoreMap with additional token-specific methods
//                for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                    // this is the text of the token
//                    String word = token.get(CoreAnnotations.TextAnnotation.class);
//                    // this is the POS tag of the token
//                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                    // this is the NER label of the token
//                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//                }
//
//                // this is the parse tree of the current sentence
//                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//
//                // this is the Stanford dependency graph of the current sentence
//                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            }

// This is the coreference link graph
// Each chain stores a set of mentions that link to each other,
// along with a method for getting the most representative mention
// Both sentence and token offsets start at 1!


            } catch (Exception e) {
                e.printStackTrace();
                Utils.sendExceptionEmail(e);
            }

        }
    }

}