/**
 * Created by kunalwagle on 18/04/2017.
 */
import React from "react";
import DigestPanelContainer from "../../containers/Digest/DigestPanelContainer";
import {DigestTopBar} from "./DigestTopBar";

export const DigestViewerPage = ({params}) => (
    <div>
        <DigestTopBar />
        <br/> <br/><br/>
        <DigestPanelContainer digestId={params.digestId}/>
    </div>
);