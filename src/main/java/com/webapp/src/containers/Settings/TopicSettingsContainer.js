/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {TopicSettings} from "../../components/Settings/TopicSettings";
import {digestChange, outletChange} from "../../actions/Settings/SettingsActions";

const mapStateToProps = (state) => {
    return {
        chosenOutlets: state.settings.chosenOutlets,
        digest: state.settings.digest,
        topicName: state.settings.topicName
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleDigestChange: (digest) => {
            dispatch(digestChange(digest));
        },
        handleOutletChange: (outlet) => {
            dispatch(outletChange(outlet));
        }
    }

};

const TopicSettingsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicSettings);

export default TopicSettingsContainer;