import React from "react"
import stub from "@/init";

export interface MenuModel {
    name: string,
    path: string
    icon?: React.ReactNode
    children?: Array<MenuModel>
}

const menus: MenuModel[] = [
    {
        name: "CF优选",
        path: "/cf-best",
        children: [
            {
                name: "CDN",
                path: "cdn",
            },
            {
                name: "server",
                path: "server"
            }
        ]
    }
];
export default menus