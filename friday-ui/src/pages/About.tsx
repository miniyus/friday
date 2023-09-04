// src/pages/About.tsx
import React from 'react';
import PageLayout from '../components/layouts/PaageLayout';
import { Paper } from '@mui/material';

const About: React.FC = () => {
    return (
        <PageLayout>
            <Paper>
                <h1>About 페이지</h1>
                <p>이곳은 어바웃 페이지입니다.</p>
            </Paper>
        </PageLayout>
    );
};

export default About;
