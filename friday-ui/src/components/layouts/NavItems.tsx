import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import React, { Fragment } from 'react';
import { Link } from 'react-router-dom';

export interface NavItemsProps {
    text: { [key: string]: string };
}

export default function NavItems(props: NavItemsProps) {
    return (
        <Fragment>
            <Box mr={2}>
                <Button color="inherit" component={Link} to="/">
                    {props.text.home}
                </Button>
            </Box>
            <Box>
                <Button color="inherit" component={Link} to="/about">
                    {props.text.about}
                </Button>
            </Box>
        </Fragment>
    );
}