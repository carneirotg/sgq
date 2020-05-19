import { format, parseISO } from 'date-fns'

export function parseDate(dt) {
    return work(dt, "dd/MM/yyyy HH:mm");
};

export function parseDateOnly(dt) {
    return work(dt, "dd/MM/yyyy");
}

function work(dt, dateFormat) {

    if (dt === null || dt === '' || dt === undefined) {
        return ''
    }

    return format(parseISO(dt.substring(0, dt.length - 9)), dateFormat);
}