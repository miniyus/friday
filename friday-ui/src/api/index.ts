import ApiClient, {
    ErrorHandler,
    ErrorResInterface,
    ErrorResponse,
    Token,
} from '@api/base/ApiClient';
import BaseClient from '@api/base/BaseClient';
import { makePath } from '@api/utils/str';
import { AxiosError, AxiosRequestHeaders } from 'axios';

interface ClientType<T> {
    new (client: ApiClient): T;

    readonly prefix: string;
}

export interface ApiConfig {
    host?: string | null;
    prefix?: string | null;
    headers?: AxiosRequestHeaders;
    token?: Token;
    errorHandler?: ErrorHandler;
}

export const isErrorResponse = (res: any): boolean => {
    if (res instanceof ErrorResponse) {
        return true;
    }

    return 'error' in res && 'code' in res && 'message' in res;
};

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

export const defaultErrorHandler: ErrorHandler = (
    error: any,
): ErrorResInterface => {
    const errorResponse: ErrorResInterface = {
        error: 'UNKNOWN_ERROR',
        code: 0,
        message: 'Unknown Error',
    };

    if (error instanceof AxiosError) {
        if (error.response?.data.error || 500 < 500) {
            errorResponse.error = error.response?.data.error || 'error';
            errorResponse.code =
                error.response?.data.code || error.response?.data.error || 0;
            errorResponse.message = error.response?.data.message || '';
        }
    }

    return {
        ...error.response,
        isSuccess: false,
        error: new ErrorResponse(errorResponse),
    };
};

const defaultConfig: ApiConfig = {
    host: 'localhost',
    prefix: '/api',
    errorHandler: defaultErrorHandler,
};

/**
 * @param {ApiConfig} apiConfig
 * @returns {ApiClient}
 */
export const api = (apiConfig: ApiConfig): ApiClient => {
    if (apiConfig?.prefix) {
        if (!apiConfig?.host) {
            apiConfig.host = defaultConfig.host as string;
        }

        try {
            const url = new URL(apiConfig.host);
            apiConfig.host =
                url.protocol +
                '//' +
                makePath(url.host + url.pathname, apiConfig.prefix);
        } catch (error) {
            const url = new URL(window.location.href);
            apiConfig.host =
                url.protocol +
                '//' +
                makePath(url.host + apiConfig.host, apiConfig.prefix);
        }
    }

    if (apiConfig?.host) {
        try {
            const url = new URL(apiConfig.host);
            apiConfig.host = url.protocol + '//' + url.host + url.pathname;
        } catch (error) {
            const url = new URL(window.location.href);
            apiConfig.host = url.protocol + '//' + url.host + apiConfig.host;
        }
    } else {
        apiConfig.host = defaultConfig.host as string;
    }

    if (!apiConfig?.errorHandler) {
        apiConfig.errorHandler = defaultConfig.errorHandler;
    }

    const client: ApiClient = new ApiClient(
        apiConfig.host,
        apiConfig.errorHandler,
    );

    if (apiConfig) {
        if (apiConfig.headers) {
            client.withHeader(apiConfig.headers);
        }
        if (apiConfig.token) {
            client.withToken(
                apiConfig.token.token as string,
                apiConfig.token.tokenType as string,
            );
        }
    }

    return client;
};

const makeClient = <T extends BaseClient>(
    client: ClientType<T>,
    config: ApiConfig,
): T => {
    return new client(api(config));
};

export default makeClient;
