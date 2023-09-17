// src/components/Navbar.tsx
import { Toolbar, Typography, IconButton, Menu, MenuItem } from '@mui/material';
import { styled } from '@mui/material/styles';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import { Menu as MenuIcon } from '@mui/icons-material';
import LanguageIcon from '@mui/icons-material/Language';
import ToggleableProps from './ToggleableProps';
import NavItems from '@app/components/layouts/NavItems';
import { useState } from 'react';
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
    text: { [key: string]: string };
    changeLang: (lng: string) => any;
}

export default function Navbar({ open, toggleDrawer, title, text, changeLang }: NavBarProps) {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    const handleClose = () => {
        setAnchorEl(null);
    }

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    }

    return (
        <AppBar position="absolute" open={open} >
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
                <NavItems text={text} />
                <IconButton
                    edge="end"
                    color="inherit"
                    aria-label="language"
                    onClick={handleClick}
                    size="large"
                >
                    <LanguageIcon />
                </IconButton>
                <Menu
                    id="menu-appbar"
                    anchorEl={anchorEl}
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}
                    keepMounted
                    transformOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                >
                    <MenuItem onClick={() => changeLang('en')}>en</MenuItem>
                    <MenuItem onClick={() => changeLang('ko')}>ko</MenuItem>
                </Menu>
            </Toolbar>
        </AppBar>
    );
};
