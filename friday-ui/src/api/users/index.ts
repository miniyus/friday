import BaseClient from "@app/api/base/BaseClient";

class UserClient extends BaseClient {
    static readonly prefix = "/v1/users";
}

export default UserClient;
