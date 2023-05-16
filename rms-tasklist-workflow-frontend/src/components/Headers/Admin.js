import React from "react";
import { useLocation, Route, Switch, Redirect } from "react-router-dom";
// reactstrap components
import { Container } from "reactstrap";
// core components

import routes from "../../routes.js";
import Appbar from "./Appbar.js";
const Admin = (props) => {
  const mainContent = React.useRef(null);
  const location = useLocation();

  React.useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    mainContent.current.scrollTop = 0;
  }, [location]);

  const getRoutes = (routes) => {
    return routes.map((prop, key) => {
      if (prop.layout === "/rms") {
        return (
          <Route
            path={prop.layout + prop.path}
            component={prop.component}
            key={key}
          />
        );
      } else {
        return null;
      }
    });
  };

  const getBrandText = (path) => {
    for (let i = 0; i < routes.length; i++) {
      if (
        props.location.pathname.indexOf(routes[i].layout + routes[i].path) !==
        -1
      ) {
        return routes[i].name;
      }
    }
    return "Brand";
  };

  return (
    <>
      <div
        className="header pb-8 pt-5"
        style={{
          maxHeight: "400px",
          backgroundImage:
            "url(" +
            require("../../assets/theme/black-wallpaper-to-set-as-background-10.jpg") +
            ")",
          backgroundSize: "cover",
          backgroundPosition: "center top",
        }}
      >
        <Appbar
          {...props}
          routes={routes}
          logo={{
            innerLink: "/rms/task/list",
            imgSrc: require("../../assets/img/brand/White-Images-Deloitte.png"),
            imgAlt: "...",
          }}
        />

        <div className="main-content py-1" ref={mainContent}>
          {/* this admin navbar is the one with name and pic inside a page */}
          {/* <AdminNavbar
          {...props}
          brandText={getBrandText(props.location.pathname)}
        /> */}
          <Switch>
            {getRoutes(routes)}
            <Redirect from="*" to="/rms/task/list" />
          </Switch>
          {/* <Container fluid>
          <AdminFooter />
        </Container> */}
        </div>
      </div>
    </>
  );
};

export default Admin;
