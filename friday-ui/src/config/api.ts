import { ApiConfig } from "@app/api";

export interface ApiConfigs {
    [key: string]: ApiConfig;
}

export default function (): ApiConfigs {
    return {
        default: {
            host: process.env.REACT_APP_API_HOST ?? "",
            prefix: process.env.REACT_APP_API_PREFIX ?? "",
        },
    };
}
