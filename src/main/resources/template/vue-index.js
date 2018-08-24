import fetch from 'utils/fetch'

export function page(query) {
    return fetch({
        url: '/api/admin/${entity}/list',
        method: 'get',
        params: query
    })
}

export function dictTree(query) {
  return fetch({
    url: '/api/admin/${entity}/dictTree',
    method: 'get',
    params: query
  })
}

export function getObj(id) {
  return fetch({
    url: '/api/admin/${entity}/get/' + id,
    method: 'get'
  })
}

export function addObj(obj) {
  return fetch({
    url: '/api/admin/${entity}/add',
    method: 'post',
    data: obj
  })
}

export function putObj(id, obj) {
  return fetch({
    url: '/api/admin/${entity}/put/' + id,
    method: 'put',
    data: obj
  })
}

export function delObj(id) {
  return fetch({
    url: '/api/admin/${entity}/deleted',
    method: 'delete',
    params: id
  })
}

