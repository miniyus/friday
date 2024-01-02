import axios, {
    AxiosResponse,
    AxiosError,
    RawAxiosRequestHeaders,
    AxiosRequestConfig,
} from 'axios';
import {makePath} from '@api/utils/str';
import {
    AfterResponseHandler,
    ApiResponse,
    Attachment,
    ErrorHandler,
    ErrorResInterface,
    PreRequestHandler,
    Token
} from "@api/base/types";

/**
 * ErrorResponse class.
 */
export class ErrorResponse implements ErrorResInterface {
    private readonly _error: string;
    private readonly _code: number;
    private readonly _message: string;

    /**
     * Constructs a new instance of the ErrorResInterface class.
     *
     * @param {ErrorResInterface} props - The properties for the ErrorResInterface.
     */
    constructor(props: ErrorResInterface) {
        this._error = props.error;
        this._code = props.code;
        this._message = props.message;
    }

    /**
     * Get the error of the object.
     *
     * @return {string} The status of the object.
     */
    get error(): string {
        return this._error;
    }

    /**
     * Returns the value of the code property.
     *
     * @return {number} The value of the code property.
     */
    get code(): number {
        return this._code;
    }

    /**
     * Get the value of the message property.
     *
     * @return {string} The value of the message property.
     */
    get message(): string {
        return this._message;
    }

    /**
     * Serializes the error response object.
     *
     * @return {ErrorResInterface} The serialized error response object.
     */
    public serialize(): ErrorResInterface {
        return {
            error: this.error,
            code: this.code,
            message: this.message,
        };
    }
}

/**
 * ApiClient class.
 *
 * Wrapping Axios
 */
export default class ApiClient {
    protected _host: string;
    protected _token: Token | null;
    protected _headers: RawAxiosRequestHeaders | null;
    protected _response: AxiosResponse | null;
    protected _attachment: Attachment[];
    protected _error: any;
    protected _isSuccess: boolean;
    protected _errorHandler: ErrorHandler | null;
    protected _preRequestHandler: PreRequestHandler | null;
    protected _afterResponseHandler: AfterResponseHandler | null;

    /**
     * @param {string} host
     * @param errorHandler
     * @param preRequestHandler
     * @param afterResponseHandler
     */
    constructor(host: string,
                errorHandler: ErrorHandler | null = null,
                preRequestHandler: PreRequestHandler | null = null,
                afterResponseHandler: AfterResponseHandler | null = null) {
        this._host = host;
        this._headers = null;
        this._response = null;
        this._token = null;
        this._error = null;
        this._isSuccess = false;
        this._attachment = [];
        this._errorHandler = errorHandler;
        this._preRequestHandler = preRequestHandler;
        this._afterResponseHandler = afterResponseHandler;
    }

    /**
     * @returns {string}
     */
    get host(): string {
        return this._host;
    }

    /**
     * get response
     * @returns {AxiosResponse<*, *>|null}
     */
    get response(): AxiosResponse<any, any> | null {
        return this._response;
    }

    /**
     * get error
     * @returns {*}
     */
    get error(): any {
        return this._error;
    }

    /**
     * request
     * @param {AxiosRequestConfig} config
     * @returns {Promise<AxiosResponse<*, *>|*>}
     */
    async request(config: AxiosRequestConfig): Promise<ApiResponse> {
        if (this._token != null) {
            let token;
            if (this._token.tokenType) {
                token = `${this._token.tokenType} ${this._token.token}`;
            } else {
                token = `${this._token.token}`;
            }

            this._headers = Object.assign(this._headers || {}, {
                Authorization: token,
            });
        }

        if (this._attachment && this._attachment.length != 0) {
            this._headers = Object.assign(this._headers || {}, {
                'Content-Type': 'multipart/form-data',
            });
            const data = new FormData();
            for (const [key, value] of Object.entries(config.data)) {
                data.append(key, value as string | Blob);
            }

            for (const attach of this._attachment) {
                data.append(attach.name, attach.file);
            }
            config.data = data;

            this._attachment = [];
        }

        if (this._headers != null) {
            config.headers = this._headers;
            this._headers = null;
        }

        if (this._preRequestHandler) {
            console.debug("call: preRequest");
            config = this._preRequestHandler(config);
        }

        let axiosResponse: AxiosResponse = await axios.request(config);
        if (this._afterResponseHandler) {
            console.debug("call: afterRequest");
            axiosResponse = this._afterResponseHandler(axiosResponse);
        }

        return this.setResponse(axiosResponse);
    }

    /**
     * make url
     * @param {string} path
     * @returns {string}
     */
    makeUrl(path: string): string {
        const [schema, host] = this.host.split('://');
        const url = makePath(host, path);
        return schema + '://' + url;
    }

    /**
     *
     * @param {AxiosResponse<*, *>} res
     * @returns {ApiResponse}
     */
    setResponse(
        res: AxiosResponse<any, any>,
    ): ApiResponse {
        try {
            this._response = res;
            this._isSuccess = true;
            return {
                ...this._response,
                isSuccess: true,
                error: null,
            };
        } catch (error: any | AxiosError | Error) {
            this._isSuccess = false;
            this._error = error;

            let errorResponse = null;
            if (this._errorHandler) {
                errorResponse = this._errorHandler(error);
            }

            return {
                ...error,
                isSuccess: false,
                error: errorResponse,
            };
        }
    }

    /**
     * Returns the value of the isSuccess property.
     * @returns {boolean}
     */
    isSuccess(): boolean {
        return this._isSuccess;
    }

    /**
     * with token
     * @param {string} token
     * @param {string|null} tokenType
     * @returns {ApiClient}
     */
    withToken(token: string, tokenType?: string): ApiClient {
        this._token = {tokenType: tokenType || null, token: token};
        return this;
    }

    /**
     * with headers
     * @param {RawAxiosRequestHeaders} headers
     * @returns {ApiClient}
     */
    withHeader(headers: RawAxiosRequestHeaders): ApiClient {
        this._headers = Object.assign(this._headers || {}, headers);
        return this;
    }

    /**
     * Attaches one or more files to the current object.
     *
     * @param {Attachment | Attachment[]} file - The file(s) to be attached. Can be a single file or an array of files.
     * @return {this} - Returns nothing.
     */
    attach(file: Attachment | Attachment[]): ApiClient {
        if (Array.isArray(file)) {
            this._attachment.concat(file);
        } else {
            this._attachment.push(file);
        }
        return this;
    }

    /**
     *
     * @param {string} path
     * @param {*} params
     * @returns {Promise<ApiResponse>|*|null>}
     */
    get(path: string, params: any = {}): Promise<ApiResponse> {
        const config: AxiosRequestConfig = {};
        config.method = 'GET';
        config.url = this.makeUrl(path);
        config.params = params;

        return this.request(config);
    }

    /**
     *
     * @param {string} path
     * @param {*} data
     * @returns {Promise<ApiResponse>}
     */
    post(path: string, data: object = {}): Promise<ApiResponse> {
        const config: AxiosRequestConfig = {};
        config.method = 'POST';
        config.url = this.makeUrl(path);
        config.data = data;

        return this.request(config);
    }

    /**
     *
     * @param {string} path
     * @param {*} data
     * @returns {Promise<ApiResponse>}
     */
    put(path: string, data: object = {}): Promise<ApiResponse> {
        const config: AxiosRequestConfig = {};
        config.method = 'PUT';
        config.url = this.makeUrl(path);
        config.data = data;

        return this.request(config);
    }

    /**
     *
     * @param {string} path
     * @param {*} data
     * @returns {Promise<ApiResponse>}
     */
    patch(path: string, data: object = {}): Promise<ApiResponse> {
        const config: AxiosRequestConfig = {};
        config.method = 'PATCH';
        config.url = this.makeUrl(path);
        config.data = data;

        return this.request(config);
    }

    /**
     *
     * @param {string} path
     * @param {*} data
     * @returns {Promise<ApiResponse>}
     */
    delete(path: string, data: object = {}): Promise<ApiResponse> {
        const config: AxiosRequestConfig = {};
        config.method = 'DELETE';
        config.url = this.makeUrl(path);
        config.data = data;

        return this.request(config);
    }
}
