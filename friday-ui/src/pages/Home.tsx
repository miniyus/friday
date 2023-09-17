// src/pages/Home.tsx
import React from 'react';
import { Typography, Box } from '@mui/material';
import PageLayout from '@app/components/layouts/PaageLayout';

const Home: React.FC = () => {
    return (
        <PageLayout>
            <Box textAlign="center" my={4}>
                <Typography variant="h4" component="h1">
                    홈 페이지
                </Typography>
                <Typography variant="body1">
                    이곳은 홈 페이지입니다.
                </Typography>
            </Box>
        </PageLayout >
    );
};

export default Home;
