/**
 * Created by kunalwagle on 15/05/2017.
 */

export const DIGEST_CHANGE = "DIGEST_CHANGE";
export const OUTLET_CHANGE = "OUTLET_CHANGE";

export const digestChange = (digest) => {
    return {
        type: DIGEST_CHANGE,
        digest: !digest
    }
};

export const outletChange = (outlet) => {
    return {
        type: OUTLET_CHANGE,
        outlet
    }
};