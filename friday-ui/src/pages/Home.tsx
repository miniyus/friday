// src/pages/Home.tsx
import React from 'react';
import { Container, Typography, Box, Grid } from '@mui/material';

const Home: React.FC = () => {
    return (
        <Grid>
            <Box textAlign="center" my={4}>
                <Typography variant="h4" component="h1">
                    홈 페이지
                </Typography>
                <Typography variant="body1">
                    이곳은 홈 페이지입니다.
                </Typography>
            </Box>
        </Grid >
    );
};

export default Home;
