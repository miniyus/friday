import {ApiConfig} from "api";

export interface ApiConfigs {
    [key: string]: ApiConfig;
}

export default ({
    default: {
        host: "/api",
        prefix: "/v1",
    },
}) as ApiConfigs;
