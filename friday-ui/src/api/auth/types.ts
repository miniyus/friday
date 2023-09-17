export interface SignupRequest {
    email: string;
    password: string;
    name: string;
}

export interface UserResource {
    id: number;
    snsId: string;
    provider: string;
    username: string;
    name: string;
    role: "USER" | "MANAGER" | "ADMIN";
    snsUser: boolean;
    attributes: { [key: string]: any };
    authorities: {
        Authority: string;
    }[];
}

export interface SigninRequest {
    username: string;
    password: string;
}

export interface Tokens {
    tokenType: string;
    accessToken: string;
    expiresIn: number;
    refreshToken: string;
}

export interface SigninResponse {
    id: number;
    username: string;
    name: string;
    tokens: Tokens;
}
