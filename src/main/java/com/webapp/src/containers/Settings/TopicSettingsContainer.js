/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {TopicSettings} from "../../components/Settings/TopicSettings";
import {digestChange, outletChange, save} from "../../actions/Settings/SettingsActions";
import {unsubscribe} from "../../actions/LoginModalActions";

const mapStateToProps = (state) => {
    return {
        chosenOutlets: state.settings.chosenOutlets,
        digest: state.settings.digest,
        topic: state.settings.topic
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleDigestChange: (digest) => {
            dispatch(digestChange(digest));
        },
        handleOutletChange: (outlet) => {
            dispatch(outletChange(outlet));
        },
        handleSave: () => {
            dispatch(save());
        },
        handleUnsubscribe: (topicId) => {
            dispatch(unsubscribe(topicId))
        }
    }

};

const TopicSettingsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicSettings);

export default TopicSettingsContainer;