import React from "react";
import {Layout} from 'antd';
import Header from '@/layout/Header'
import Content from '@/layout/Content'

const Home: React.FC<any> = (props: any) => {
    return (
            <Layout>
                <Header/>
                <Layout className={"container"}>
                    <Content/>
                </Layout>
            </Layout>
    );
}
export default Home