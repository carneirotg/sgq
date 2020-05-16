import { format, parseISO } from 'date-fns'

export function parseDate(dt) {

    if (dt === null || dt === '') {
        return ''
    }

    return format(parseISO(dt.substring(0, dt.length - 9)), "dd/MM/yyyy HH:mm");
};