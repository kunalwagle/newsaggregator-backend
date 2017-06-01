/**
 * Created by kunalwagle on 04/05/2017.
 */

export const getIPAddress = () => {
    return "localhost:8183/api/";
};

export const getColour = (source) => {
    switch (source) {
        case "the-guardian-uk":
            return "teal";
        case "independent":
            return "salmon";
        case "associated-press":
            return "lightgreen";
        case "reuters":
            return "brown";
        case "business-insider-uk":
            return "green";
        case "daily-mail":
            return "purple";
        case "espn-cric-info":
            return "lightblue";
        case "metro":
            return "gold";
        case "mirror":
            return "red";
        case "newsweek":
            return "magenta";
        case "the-telegraph":
            return "darkblue";
        case "the-times-of-india":
            return "orange";
        case "bbc-news":
            return "maroon";
        case "bbc-sport":
            return "gold";
        case "bloomberg":
            return "blue";
        case "cnbc":
            return "darkgreen";
        case "cnn":
            return "firebrick";
        case "espn":
            return "mediumseagreen";
        case "four-four-two":
            return "peru";
        case "the-washington-post":
            return "steelblue";
        default:
            return "black";
    }
};

export const getPublicationName = (source) => {
    switch (source) {
        case "the-guardian-uk":
            return "The Guardian";
        case "independent":
            return "The Independent";
        case "associated-press":
            return "Associated Press";
        case "reuters":
            return "Reuters";
        case "business-insider-uk":
            return "Business Insider";
        case "daily-mail":
            return "The Daily Mail";
        case "espn-cric-info":
            return "ESPN Cricinfo";
        case "metro":
            return "Metro";
        case "mirror":
            return "The Mirror";
        case "newsweek":
            return "Newsweek";
        case "the-telegraph":
            return "The Telegraph";
        case "the-times-of-india":
            return "The Times of India";
        case "bbc-news":
            return "BBC News";
        case "bbc-sport":
            return "BBC Sport";
        case "bloomberg":
            return "Bloomberg";
        case "cnbc":
            return "CNBC";
        case "cnn":
            return "CNN";
        case "espn":
            return "ESPN";
        case "four-four-two":
            return "FourFourTwo";
        case "the-washington-post":
            return "The Washington Post";
        default:
            return "";
    }
};

export const allOutlets = ["associated-press", "the-guardian-uk", "independent", "reuters", "business-insider-uk", "daily-mail", "espn-cric-info", "metro", "mirror", "newsweek", "the-telegraph", "the-times-of-india", "bbc-news", "bbc-sport", "bloomberg", "cnbc", "cnn", "espn", "four-four-two", "the-washington-post"];