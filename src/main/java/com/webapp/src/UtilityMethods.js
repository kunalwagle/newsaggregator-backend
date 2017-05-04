/**
 * Created by kunalwagle on 04/05/2017.
 */

export const getIPAddress = () => {
    return "http://178.62.27.53:8182/api/";
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