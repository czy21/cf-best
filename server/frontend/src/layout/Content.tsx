import React from "react";
import { Breadcrumb, Layout, Menu, theme } from 'antd';

const AntdContent = Layout.Content;

const Content: React.FC = () => {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();
    return (

        <AntdContent >
                Content
        </AntdContent>
    )
}

export default Content