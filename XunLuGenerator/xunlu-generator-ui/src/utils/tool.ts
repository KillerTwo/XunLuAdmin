import type { App, Plugin, Component } from 'vue'
import { AES, lib, enc, mode, pad } from 'crypto-js'
import {JSEncrypt} from "jsencrypt";

// 全局组件安装
export const withInstall = <T extends Component>(component: T, alias?: string) => {
	const comp = component as any
	comp.install = (app: App) => {
		app.component(comp.__name || comp.displayName, component)
		if (alias) {
			app.config.globalProperties[alias] = component
		}
	}
	return component as T & Plugin
}

// 密钥
const ENCRYPT_KEY = 'makulowcodeyyds1'

export const decrypt = (ciphertext: string): string => {
	// 将密文转换为CipherParams对象
	const cipherParams = lib.CipherParams.create({
		ciphertext: enc.Base64.parse(ciphertext)
	})

	// 使用密钥解密CipherParams对象
	const decrypted = AES.decrypt(cipherParams, enc.Utf8.parse(ENCRYPT_KEY), {
		mode: mode.ECB,
		padding: pad.Pkcs7
	})

	// 获取明文
	return decrypted.toString(enc.Utf8)
}

export const encrypt = (plaintext: string): string => {
	// 将明文转换为要加密的格式
	const message = enc.Utf8.parse(plaintext)

	// 使用密钥加密明文
	const encrypted = AES.encrypt(message, enc.Utf8.parse(ENCRYPT_KEY), {
		mode: mode.ECB,
		padding: pad.Pkcs7
	})

	return encrypted.toString()
}

const PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl3apLMAXkXHtyfHgdRAJ
MyXljj338mR3ZNfRQMiRFOJmeKf8mLepr4R/dkn3rxxFxlXWUac/YjzXtQ/wjUpd
z+u4JLnvHTZj6BhOGOBEB1KRGbvHu1nU3eC4Lg1asmdWPK2tSODZRwubJnnSAiOT
XG3XN6nb4zelTh4+0MrKsA6gUKR6livo6tE4WzXFYTyHF4PwVSf3kc3PqkwkSe32
kSbt2au/aqRh6Kx9Upcj1FUdQUMDNElFgDbhK3GrouVIHFYcvR996hWRmNLW06p/
95c7cQdBKdsa/p6L28yPyRTXFk4w1ezTLLx4ZBeSgjiMCIJHvHWgclvrMdHypAEC
FwIDAQAB
-----END PUBLIC KEY-----`

export const encryptWithPublicKey = (plaintext:string)=> {
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(PUBLIC_KEY);
    return encrypt.encrypt(plaintext);
}
