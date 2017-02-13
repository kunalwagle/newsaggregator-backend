package test.java;

import com.newsaggregator.base.Topic;
import com.newsaggregator.ml.labelling.TopicLabelling;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by kunalwagle on 13/02/2017.
 */
public class TopicLabellingTest {

    private Topic topic = Mockito.mock(Topic.class);

    @Test(expected = IndexOutOfBoundsException.class)
    public void generateTopicLabel() throws Exception {
        TopicLabelling.generateTopicLabel(topic);
    }

}