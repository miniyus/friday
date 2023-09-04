import axios from 'axios';

export const limit = (value: string, limit: number, end = '...'): string => {
    let limitedStr = value;

    if (value) {
        if (!limit) {
            return value;
        }

        if (value.length > limit) {
            limitedStr = value.substring(0, limit) + end;
        }
    }

    return limitedStr;
};

export const limitArray = (
    arr: string[],
    limit: number,
    end = '...',
): string => {
    const reArr = arr.filter((str, i) => {
        return i < limit;
    });

    return reArr.join(', ') + end;
};

export const makePath = (source: string, target: string): string => {
    const path = source + '/' + target;
    return path.replaceAll('//', '/');
};

export const isString = (value: any) => {
    return typeof value === 'string' || value instanceof String;
};

export const toBlob = async (blobUrl: string): Promise<Blob | null> => {
    if (blobUrl.includes('blob:', 0)) {
        const blobRes = await axios.get(blobUrl, {
            responseType: 'blob',
        });

        return blobRes.data;
    }

    return null;
};

export const parseAttachFileName = (str: string) => {
    return str.split(';')[1].trim().split('=')[1].replaceAll('"', '');
};

export const replaceAll = (
    str: string,
    replace: { [key: string]: string },
    format: string[] = ['{{', '}}'],
) => {
    let reStr = '';
    for (const [k, v] of Object.entries(replace)) {
        reStr = str.replaceAll(format[0] + k + format[1], v);
    }

    return reStr;
};

export const decodeUnicode = (unicodeString: string): string => {
    const r = /\\u([\d\w]{4})/gi;

    unicodeString = decodeURI(unicodeString);
    unicodeString = unicodeString.replace(r, function (match, grp) {
        return String.fromCharCode(parseInt(grp, 16));
    });
    return unicodeString;
};