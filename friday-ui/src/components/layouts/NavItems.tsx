import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import React, { Fragment } from 'react';
import { Link } from 'react-router-dom';

export default function NavItems() {
    return (
        <Fragment>
            <Box mr={2}>
                <Button color="inherit" component={Link} to="/">
                    홈
                </Button>
            </Box>
            <Box>
                <Button color="inherit" component={Link} to="/about">
                    About
                </Button>
            </Box>
            <Box>
                <Button color="inherit" component={Link} to="/hosts">
                    Hosts
                </Button>
            </Box>
        </Fragment>
    );
}