/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";


const mapStateToProps = () => {
    return {}
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
