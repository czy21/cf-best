import React from "react";

import { Breadcrumb, Layout, Menu, theme } from 'antd';
const AntdHeader = Layout.Header


const items = new Array(3).fill(null).map((_, index) => ({
    key: String(index + 1),
    label: `nav ${index + 1}`,
}));

export default class Header extends React.Component<any, any> {
    render() {
        return (
            <AntdHeader
                style={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 1,
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                }}
            >
                <Menu
                    theme="dark"
                    mode="horizontal"
                    defaultSelectedKeys={['2']}
                    items={items}
                    style={{ flex: 1, minWidth: 0 }}
                />
            </AntdHeader>
        )
    }
}