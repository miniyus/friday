import React, { ReactNode } from 'react';
import { Box, Grid } from '@mui/material';

const PageLayout: React.FC<{ children: ReactNode }> = ({ children }) => {
    return (
        <Grid>
            <Box textAlign="center" my={5}>
                {children}
            </Box>
        </Grid>
    );
};

export default PageLayout;
