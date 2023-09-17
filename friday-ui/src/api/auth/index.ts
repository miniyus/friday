import {
    SigninRequest,
    SignupRequest,
    UserResource,
} from "@app/api/auth/types";
import { ApiResponse, ErrorResInterface } from "@app/api/base/ApiClient";
import BaseClient from "@app/api/base/BaseClient";

class AuthClient extends BaseClient {
    static readonly prefix = "/v1/auth";

    public async signup(
        request: SignupRequest
    ): Promise<ApiResponse<UserResource | ErrorResInterface>> {
        return await this._client.post("/signup", request);
    }

    public async signin(
        request: SigninRequest
    ): Promise<ApiResponse<UserResource | ErrorResInterface>> {
        return await this._client.post("/signin", request);
    }
}

export default AuthClient;
