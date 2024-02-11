import React from "react";

import {Layout, Menu} from 'antd';
import {Link} from "react-router-dom";
import stub from "@/init";
import menu from "@/menu";


function recursiveMenu(routes: any, parentPath?: string) {
    return routes.map((item: any, index: any) => {
        if (item.children) {
            return (
                <Menu.SubMenu
                    key={index}
                    title={
                        <span>
                            {item.icon}
                            <span>{item.name}</span>
                        </span>
                    }
                >
                    {recursiveMenu(item.children, item.path)}
                </Menu.SubMenu>
            )
        }
        return (
            <Menu.Item
                key={index}
            >
                {item.icon}
                <span>{item.name}</span>
                <Link to={parentPath != null ? stub.ref.lodash.join([parentPath, item.path], "/") : item.path}/>
            </Menu.Item>
        )
    })
}

export default class Header extends React.Component<any, any> {
    render() {
        return (
            <Layout.Header
                style={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 1,
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                }}
            >
                <Menu>
                    {recursiveMenu(menu)}
                </Menu>
            </Layout.Header>
        )
    }
}