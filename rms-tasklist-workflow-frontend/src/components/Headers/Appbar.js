import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import Container from "@mui/material/Container";
import Avatar from "@mui/material/Avatar";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@mui/material/MenuItem";

import { NavLink as NavLinkRRD, Link, NavLink } from "react-router-dom";
import { Input, InputGroup, InputGroupAddon, InputGroupText } from "reactstrap";

const settings = ["Profile", "Account", "Dashboard", "Logout"];

function Appbar(props) {
  const [anchorElNav, setAnchorElNav] = React.useState(null);
  const [anchorElUser, setAnchorElUser] = React.useState(null);

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const defaultStyle = {
    borderRadius: "0",
    margin: "5px",
    padding: "5px",
    borderBottom: "none",
    fontWeight: "bold",
    // fontSize: "12px",
    color: "gray",
  };

  const activeStyle = {
    borderBottom: "2px solid #88BC4A",
    color: "white",
  };

  // creates the links that appear in the left menu / Sidebar
  const createLinks = (routes) => {
    return routes.map((prop, key) => {
      if (
        prop.path !== "/task/details" &&
        prop.path !== "/task/setting/task-type/creation" &&
        prop.path !== "/task/setting/task-type/detail"
      ) {
        return (
          <div key={key}>
            <NavLink
              to={prop.layout + prop.path}
              tag={NavLinkRRD}
              style={defaultStyle}
              activeStyle={activeStyle}
            >
              {prop.name}
            </NavLink>
          </div>
        );
      }
    });
  };

  const { bgColor, routes, logo } = props;
  let navbarBrandProps;
  if (logo && logo.innerLink) {
    navbarBrandProps = {
      to: logo.innerLink,
      tag: Link,
    };
  } else if (logo && logo.outterLink) {
    navbarBrandProps = {
      href: logo.outterLink,
      target: "_blank",
    };
  }

  return (
    <AppBar position="fixed" style={{ backgroundColor: "black" }}>
      <Container maxWidth="auto">
        <Toolbar disableGutters>
          {logo ? (
            <>
              <div className="pt-0 pl-0" {...navbarBrandProps}>
                <img
                  alt={logo.imgAlt}
                  className="navbar-brand-img"
                  src={logo.imgSrc}
                  style={{
                    width: "150px",
                    height: "auto",
                    paddingRight: "20px",
                  }}
                />
              </div>

              <h4
                style={{
                  color: "white",
                  borderLeft: "1px solid white",
                  paddingLeft: "20px",
                  margin: "0 auto",
                }}
              >
                ICDC RMS
              </h4>
            </>
          ) : null}

          {/* pages */}
          <Box
            sx={{
              paddingLeft: "30px",
              flexGrow: 1,
              display: { xs: "none", md: "flex" },
            }}
          >
            {createLinks(routes)}
          </Box>

          {/* <Box
            style={{
              display: "flex",
              paddingRight: "20px",
              paddingLeft: "20px",
            }}
          >
            <InputGroup className="input-group-rounded input-group-merge">
              <InputGroupAddon addonType="prepend">
                <InputGroupText>
                  <span className="fa fa-search" />
                </InputGroupText>
              </InputGroupAddon>
              <Input
                aria-label="Search"
                className="form-control-rounded form-control-prepended"
                placeholder="Search..."
                type="text"
              />
            </InputGroup>
          </Box> */}

          <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
              <IconButton
                onClick={handleOpenUserMenu}
                sx={{ p: 0, fontSize: "5px" }}
              >
                <Avatar
                  alt="Selina"
                  src="../../assets/img/brand/deloitte-logo.png"
                />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: "45px" }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {settings.map((setting) => (
                <MenuItem key={setting} onClick={handleCloseUserMenu}>
                  <Typography textAlign="center">{setting}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default Appbar;
