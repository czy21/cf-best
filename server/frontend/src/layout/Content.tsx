import React from "react";
import {Layout} from 'antd';
import {useRoutes} from "react-router-dom";
import routes from "@/route";

const AntdContent = Layout.Content;

const Content: React.FC = () => {
    return (
        <AntdContent>
            {useRoutes(routes)}
        </AntdContent>
    )
}

export default Content