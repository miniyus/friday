import {AxiosError, AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig} from "axios";

/**
 * Api Response Type.
 *
 * Wrapping AxiosResponse
 */
export interface ApiResponse<T = any, D = any> extends AxiosResponse<T, D> {
    data: T;
    config: InternalAxiosRequestConfig<D>;
    isSuccess: boolean;
    error: ErrorResInterface | null;
}

/**
 * Attachment Type.
 */
export interface Attachment {
    name: string;
    file: File;
}

/**
 * Error Response Type.
 */
export interface ErrorResInterface {
    error: string;
    code: number;
    message: string;
}

/**
 * Error Handler Type.
 */
export interface ErrorHandler {
    (error: any | AxiosError | Error): ErrorResInterface;
}

/**
 * Token Response Handler Type.
 */
export interface TokenResponseHandler {
    (res: AxiosResponse<any, any>): void;
}

/**
 * Token Type.
 */
export interface Token {
    tokenType: string | null;
    token: string | null;
    tokenResponseHandler?: TokenResponseHandler | null;
}

export interface PreRequestHandler {
    (config: AxiosRequestConfig<any>): AxiosRequestConfig<any>;
}

export interface AfterResponseHandler {
    (response: AxiosResponse<any, any>): AxiosResponse<any, any>;
}
