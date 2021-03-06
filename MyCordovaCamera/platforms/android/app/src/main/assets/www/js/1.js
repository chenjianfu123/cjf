 new VConsole()

            const request = () => {
              console.log('request -> start')
              let url = 'https://220.241.39.45/cibs/chbank/service/v1/test'

              axios.get(url).then(res => {
                console.log('request -> success', res)
                alert(res.data.message)
              }).catch(e => {
                console.log('request -> error', e)
              })
            }

            const webSdkInit = () => {
              console.log('webSdkInit -> start')
              let url = 'http://101.132.119.74:8081/api/realIdDemoService/initialize'

              const href = window.location.href
              const baseUrl = href.split('#')[0]
              const callbackUrl = baseUrl.replace('index.html', 'result.html')

              let data = {
                flowType: 'H5_REALIDLITE_KYC',
                docType: '00860000001',
                serviceLevel: 'REALID0001',
                operationMode: 'STANDARD',
                metaInfo: 'MOB_H5',
                h5ModeConfig: {
                  completeCallbackUrl: callbackUrl,
                  interruptCallbackUrl: callbackUrl
                }
              }

              axios.post(url, data).then(res => {
                console.log('webSdkInit -> success', res)

                const idrecognizeUrl = 'https://sg-production-cdn.zoloz.com/page/zoloz-realid-fe/index.html'
                const clientCfg = res.data.clientCfg
                const state = res.data.transactionId

                window.location.href =
                  `${idrecognizeUrl}?state=${state}&clientcfg=${encodeURIComponent(clientCfg)}&callbackurl=${encodeURIComponent(callbackUrl)}`
              }).catch(e => {
                console.log('webSdkInit -> error', e)
              })
            }

            const appsdkRequest = metaInfo => {
              console.log('appsdkRequest -> start')
              let url = 'http://101.132.119.74:8081/api/realIdDemoService/initialize'

              const href = window.location.href
              const baseUrl = href.split('#')[0]
              const callbackUrl = baseUrl.replace('index.html', 'result.html')

              let data = {
                flowType: 'REALIDLITE_KYC',
                docType: '00860000001',
                serviceLevel: 'REALID0001',
                operationMode: 'STANDARD',
                metaInfo,
              }

              axios.post(url, data).then(res => {
                console.log('appsdkRequest -> success', res)

                let successCallback = ress => {
                  console.log('zolozEkyc -> successCallback', ress)
                  let response = {
                    subCode: '', // bizID
                    state: res.data.transactionId  // transactionId
                  }
                  window.location.href =
                  `${callbackUrl}?response=${encodeURIComponent(clientCfg)}`
                }

                let failureCallback = error => {
                  console.log('zolozEkyc -> failureCallback', error)
                }

                const clientCfg = res.data.clientCfg
                const state = res.data.transactionId

                let params = {
                  lang: 'zh-CN',
                  transactionId: state,
                  clientCfg,
                  rsaPubKey: res.data.rsaPubKey
                }

                cordova.plugins.ZolozEkycPlugin.zolozEkyc(params, successCallback, failureCallback);

                // const idrecognizeUrl = 'https://sg-production-cdn.zoloz.com/page/zoloz-realid-fe/index.html'


                // window.location.href =
                //   `${idrecognizeUrl}?state=${state}&clientcfg=${encodeURIComponent(clientCfg)}&callbackurl=${encodeURIComponent(callbackUrl)}`
              }).catch(e => {
                console.log('appsdkRequest -> error', e)
              })

            }

            const appsdkInit = () => {
              console.log('appsdkInit -> start')
              console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
              let successCallback = res => {
                console.log('getMetaInfo -> successCallback', res)

                if (res) {
                  let metaInfo = res
                  appsdkRequest(metaInfo)
                }
              }

              let failureCallback = error => {
                console.log('getMetaInfo -> failureCallback', error)
              }

              console.log('cordova.plugins.ZolozEkycPlugin',cordova.plugins.ZolozEkycPlugin)
              cordova.plugins.ZolozEkycPlugin.getMetaInfo('111', successCallback, failureCallback);
            }

            const init = () => {
              let requestBtn = document.getElementsByClassName('request')[0]
              let websdkBtn = document.getElementsByClassName('websdk')[0]
              let appsdkBtn = document.getElementsById('appsdk')

              requestBtn.addEventListener('click', () => {
                request()
              })
              websdkBtn.addEventListener('click', () => {
                webSdkInit()
              })
              appsdkBtn.addEventListener('click', () => {
                appsdkInit()
              })
            }

            // ??????js??????
            const dynamicLoadJs = (url, callback) => {
              let head = document.getElementsByTagName('head')[0];
              let script = document.createElement('script');
              script.type = 'text/javascript';
              script.src = url;
              if (typeof (callback) == 'function') {
                script.onload = script.onreadystatechange = function () {
                  if (!this.readyState || this.readyState === "loaded" || this.readyState === "complete") {
                    callback();
                    script.onload = script.onreadystatechange = null;
                  }
                };
              }
              head.appendChild(script);
            }

            // ??????????????????
            const getRuntime = () => {
              // debugger
              let params = new URL(location.href).searchParams

              let runtime = decodeURIComponent(params.get('runtime'))
              if (runtime === 'app') {
                //?????????js????????????
                let url = 'cordova.js'
                dynamicLoadJs(url)

                console.log('runtime', runtime)

                document.addEventListener('deviceready', init, false);
              } else {
                init()
              }


            }

            getRuntime()