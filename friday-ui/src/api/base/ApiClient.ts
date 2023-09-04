import axios, {
    AxiosResponse,
    AxiosError,
    RawAxiosRequestHeaders,
    AxiosRequestConfig,
    InternalAxiosRequestConfig,
} from 'axios';
import { makePath } from '../utils/str';

export interface Token {
    tokenType: string | null;
    token: string | null;
}

export interface ApiResponse<T = any, D = any> extends AxiosResponse<T, D> {
    data: T;
    config: InternalAxiosRequestConfig<D>;
    isSuccess: boolean;
    error: ErrorResInterface | null;
}

export interface ErrorResInterface {
    status: string;
    code: number;
    message: string;
}

export interface Attachment {
    name: string;
    file: File;
}

export class ErrorResponse implements ErrorResInterface {
    private readonly _status: string;
    private readonly _code: number;
    private readonly _message: string;

    constructor(props: ErrorResInterface) {
        this._status = props.status;
        this._code = props.code;
        this._message = props.message;
    }

    get status(): string {
        return this._status;
    }

    get code(): number {
        return this._code;
    }

    get message(): string {
        return this._message;
    }

    public serialize(): ErrorResInterface {
        return {
            status: this.status,
            code: this.code,
            message: this.message,
        };
    }
}

export default class ApiClient {
    protected _host: string;
    protected _token: Token | null;
    protected _headers: RawAxiosRequestHeaders | null;
    protected _response: AxiosResponse | null;
    protected _attachment: Attachment[];
    protected _error: any;
    protected _isSuccess: boolean;

    /**
     * @param {string} host
     */
    constructor(host: string) {
        this._host = host;
        this._headers = null;
        this._response = null;
        this._token = null;
        this._error = null;
        this._isSuccess = false;
        this._attachment = [];
    }

    /**
     * @returns {string}
     */
    get host(): string {
        return this._host;
    }

    /**
     *
     * @returns {AxiosResponse<*, *>|null}
     */
    get response(): AxiosResponse<any, any> | null {
        return this._response;
    }

    /**
     *
     * @returns {*}
     */
    get error(): any {
        return this._error;
    }

    /**
     *
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

        return this.setResponse(axios.request(config));
    }

    /**
     *
     * @param {string} path
     * @returns {string}
     */
    makeUrl(path: string) {
        const [schema, host] = this.host.split('://');
        const url = makePath(host, path);
        return schema + '://' + url;
    }

    /**
     *
     * @param {Promise<AxiosResponse<*, *>>} res
     * @returns {Promise<ApiResponse>}
     */
    async setResponse(
        res: Promise<AxiosResponse<any, any>>,
    ): Promise<ApiResponse> {
        try {
            this._response = await res;
            this._isSuccess = true;
            return {
                ...this._response,
                isSuccess: true,
                error: null,
            };
        } catch (error: any | AxiosError) {
            this._isSuccess = false;
            this._error = error;
            const errorResponse: ErrorResInterface = {
                status: 'error',
                code: 999,
                message: '관리자에게 문의해주세요.',
            };

            if (error instanceof AxiosError) {
                if (error.response?.status || 500 < 500) {
                    errorResponse.status =
                        error.response?.data.status || 'error';
                    errorResponse.code =
                        error.response?.data.code ||
                        error.response?.status ||
                        999;

                    if (
                        this.error.response.data.hasOwnProperty('failed_fields')
                    ) {
                        const messages = [];
                        for (const [key, value] of Object.entries(
                            error.response?.data.failed_fields,
                        )) {
                            console.debug(
                                `failed_key: ${key}, failed_value: ${value}`,
                            );
                            messages.push(value);
                        }
                        errorResponse.message = messages.join(', ');
                    } else {
                        errorResponse.message =
                            error.response?.data.message ||
                            error.response?.statusText ||
                            '';
                    }
                }
            }

            return {
                ...error.response,
                isSuccess: false,
                error: new ErrorResponse(errorResponse),
            };
        }
    }

    /**
     *
     * @returns {boolean}
     */
    isSuccess(): boolean {
        return this._isSuccess;
    }

    /**
     *
     * @param {string} token
     * @param {string|null} tokenType
     * @returns {ApiClient}
     */
    withToken(token: string, tokenType?: string): ApiClient {
        this._token = { tokenType: tokenType || null, token: token };
        return this;
    }

    /**
     *
     * @param {RawAxiosRequestHeaders} headers
     * @returns {ApiClient}
     */
    withHeader(headers: RawAxiosRequestHeaders): ApiClient {
        this._headers = Object.assign(this._headers || {}, headers);
        return this;
    }

    attach(file: Attachment | Attachment[]) {
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