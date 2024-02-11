import {RouteObject} from "react-router-dom";
import CFBestServer from "@v/cf-best/server";
import CFBestCDN from "@v/cf-best/cdn";

const routes: RouteObject[] = [
    {
        path: "cf-best",
        children: [
            {
                path: "cdn",
                element: <CFBestCDN/>
            },
            {
                path: "server",
                element: <CFBestServer/>
            }
        ]
    }
];
export default routes