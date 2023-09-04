import { ListItemButton, ListItemText } from "@mui/material";
import { Fragment } from "react";
import { Link } from "react-router-dom";

export default function MenuList() {
    return (
        <Fragment>
            <ListItemButton component={Link} to="/">
                <ListItemText primary="홈" />
            </ListItemButton>
            <ListItemButton component={Link} to="/about">
                <ListItemText primary="About" />
            </ListItemButton>
            <ListItemButton component={Link} to="/hosts">
                <ListItemText primary="Hosts" />
            </ListItemButton>
        </Fragment>
    )
}