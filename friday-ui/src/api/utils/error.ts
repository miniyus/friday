import {ErrorResInterface} from "@api/base/types";
import {ErrorResponse} from "@api/base/ApiClient";

/**
 * Checks if the given response is an error response.
 *
 * @param {any} res - The response object to check.
 * @return {boolean} Returns true if the response is an error response, false otherwise.
 */
export const isErrorResponse = (res: any): boolean => {
    if (res instanceof ErrorResponse) {
        return true;
    }

    return 'error' in res && 'code' in res && 'message' in res;
};

/**
 * Serializes the error response.
 *
 * @param {any} res - the response object
 * @return {ErrorResInterface} the serialized error response
 */
export const serializeErrorResponse = (res: any): ErrorResInterface => {
    if (res instanceof ErrorResponse) {
        return res.serialize();
    }

    if ('error' in res && 'code' in res && 'message' in res) {
        return res;
    } else {
        return {
            error: 'Unknown Error',
            code: 0,
            message: 'Unknown Error',
        };
    }
};
