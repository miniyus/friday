import ApiClient from "@api/base/ApiClient";

abstract class BaseClient {
    protected _client: ApiClient;
    static readonly prefix: string;

    constructor(client: ApiClient) {
        this._client = client;
    }
}

export default BaseClient;
