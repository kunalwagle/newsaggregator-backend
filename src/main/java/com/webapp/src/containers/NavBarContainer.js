/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";
import {showModal} from "../actions/LoginModalActions";

const mapStateToProps = () => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleSelect: () => {
            dispatch(showModal());
        }
    }
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
