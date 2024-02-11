import {AxiosResponse} from "axios";

const urlPattern = /^(http:\/\/?)([0-9a-z.]+)(:[0-9]+)?([/0-9a-z.-]+)?(\?[0-9a-z&=]+)?(#[0-9-a-z]+)?/i

interface URI {
    url: string,
    protocol: string,
    hostname: string,
    port: string,
    path: string
}

export function matchUrl(url: string): URI {
    let ret: any = urlPattern.exec(url)
    console.log(ret)
    return {
        "url": ret[0],
        "protocol": ret[1],
        "hostname": ret[2],
        "port": ret[3],
        "path": ret[4]
    }
}

export function downloadFile(res: AxiosResponse, fileName?: string) {
    let url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.style.display = 'none'
    a.download = decodeURIComponent(res.headers?.filename ?? fileName)
    a.href = url
    a.click()
    URL.revokeObjectURL(url);
}