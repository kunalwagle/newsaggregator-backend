/**
 * Created by kunalwagle on 04/05/2017.
 */

export const getIPAddress = () => {
    return "http://localhost:8182/api/";
};

export const getColour = (source) => {
    switch (source) {
        case "the-guardian-uk":
            return "teal";
        case "independent":
            return "yellow";
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
        default:
            return "";
    }
};