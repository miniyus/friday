// src/components/Navbar.tsx
import React from 'react';
import { Link } from 'react-router-dom';
import { Toolbar, Typography, Button, Box, IconButton } from '@mui/material';
import { styled } from '@mui/material/styles';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import { Menu as MenuIcon } from '@mui/icons-material';
import ToggleableProps from './ToggleableProps';
import { title } from 'process';
const drawerWidth: number = 240;

interface AppBarProps extends MuiAppBarProps {
    open?: boolean;
}

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})<AppBarProps>(({ theme, open }) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

interface NavBarProps extends ToggleableProps {
    title: string;
}

const Navbar: React.FC<NavBarProps> = ({ open, toggleDrawer, title }) => {
    return (
        <AppBar position="absolute" open={open}>
            <Toolbar
                sx={{
                    pr: '24px', // keep right padding when drawer closed
                }}
            >
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="open drawer"
                    onClick={toggleDrawer}
                    sx={{
                        marginRight: '36px',
                        ...(open && { display: 'none' }),
                    }}
                >
                    <MenuIcon />
                </IconButton>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    {title}
                </Typography>
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
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;