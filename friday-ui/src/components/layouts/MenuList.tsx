import { ListItemButton, ListItemText } from "@mui/material";
import { Fragment } from "react";
import { Link } from "react-router-dom";

export interface MenuListProps {
    text: { [key: string]: string };
}

export default function MenuList(props: MenuListProps) {
    return (
        <Fragment>
            <ListItemButton component={Link} to="/">
                <ListItemText primary={props.text.home} />
            </ListItemButton>
            <ListItemButton component={Link} to="/about">
                <ListItemText primary={props.text.about} />
            </ListItemButton>
            <ListItemButton component={Link} to="/hosts">
                <ListItemText primary={props.text.hosts} />
            </ListItemButton>
            <ListItemButton component={Link} to="/search">
                <ListItemText primary={props.text.search} />
            </ListItemButton>
        </Fragment>
    )
}