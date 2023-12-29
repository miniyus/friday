import makeClient from 'api';
import AuthClient from '@api/auth';

describe('auth api tests', () => {
    it('signup', async () => {
        const authClient = makeClient<AuthClient>(AuthClient, {});
        const res = await authClient.signup({
            email: 'test@email.com',
            password: 'test',
            name: 'test',
        });

        expect(res.status).toBe(200);
        expect(res.isSuccess).toBe(true);
        expect(res.data).not.toBeNull();
    });
});
