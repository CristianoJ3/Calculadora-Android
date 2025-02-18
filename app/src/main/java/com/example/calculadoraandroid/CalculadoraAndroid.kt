package com.example.calculadoraandroid

import android.app.Activity
import android.opengl.GLES31
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadoraandroid.databinding.CalculadoraAndroidActivityBinding
import java.util.Stack

class CalculadoraAndroid : AppCompatActivity() {

    private val binding by lazy {
        CalculadoraAndroidActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotoesCalculadora()
    }

    fun calcularExpressao(expressao: String): Double {
        val tokens = tokenize(expressao) // Converte a string em tokens processáveis
        val numeros = Stack<Double>()
        val operadores = Stack<Char>()

        for (token in tokens) {
            when {
                token.isDouble() -> numeros.push(token.toDouble()) // Se for número, adiciona na pilha
                token == "(" -> operadores.push(token[0]) // Se for "(", adiciona à pilha de operadores
                token == ")" -> { // Se for ")", processa até encontrar "("
                    while (operadores.isNotEmpty() && operadores.peek() != '(') {
                        processarOperacao(numeros, operadores)
                    }
                    operadores.pop() // Remove o "(" da pilha
                }
                token[0] in "+-*/" -> {
                    while (operadores.isNotEmpty() && prioridade(operadores.peek()) >= prioridade(token[0])) {
                        processarOperacao(numeros, operadores)
                    }
                    operadores.push(token[0])
                }
            }
        }

        while (operadores.isNotEmpty()) {
            processarOperacao(numeros, operadores)
        }

        return if (numeros.isNotEmpty()) numeros.pop() else 0.0
    }

    // Função para quebrar a expressão em tokens (números, operadores e parênteses)
    fun tokenize(expressao: String): List<String> {
        val tokens = mutableListOf<String>()
        var numero = ""

        for (char in expressao) {
            when {
                char.isDigit() || char == '.' -> numero += char // Acumula números e decimais
                char in "+-*/()" -> {
                    if (numero.isNotEmpty()) {
                        tokens.add(numero)
                        numero = ""
                    }
                    tokens.add(char.toString()) // Adiciona operador ou parêntese como token separado
                }
            }
        }

        if (numero.isNotEmpty()) {
            tokens.add(numero)
        }

        return tokens
    }

    // Checa a prioridade dos operadores
    fun prioridade(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    // Processa a operação e empilha o resultado
    fun processarOperacao(numeros: Stack<Double>, operadores: Stack<Char>) {
        if (numeros.size < 2) return
        val b = numeros.pop()
        val a = numeros.pop()
        val op = operadores.pop()

        val resultado = when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else Double.NaN // Evita divisão por zero
            else -> 0.0
        }
        numeros.push(resultado)
    }

    // Função de extensão para verificar se a string pode ser convertida para Double
    fun String.isDouble(): Boolean {
        return this.toDoubleOrNull() != null
    }


    private fun configuraBotoesCalculadora(){

        val textoCalculadora = binding.textoCalculadora

        val botoesNumericos = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton,
            binding.zeroButton
        )

        val botoesOperacoes = listOf(
            binding.plusButton,
            binding.minusButton,
            binding.timesButton,
            binding.divisionButton,
            binding.percentButton,
            binding.leftParentesesButton,
            binding.rightParentesesButton,
            binding.dotButton
        )

        // Adiciona número ao campo de texto
        botoesNumericos.forEach { botao ->
            botao.setOnClickListener {
                binding.textoCalculadora.append(botao.text)
            }
        }

        // Adiciona operadores com validação
        botoesOperacoes.forEach { botao ->
            botao.setOnClickListener {
                val textoAtual = binding.textoCalculadora.text.toString()

                if (textoAtual.isNotEmpty() && textoAtual.last().isDigit()) {
                    binding.textoCalculadora.append(botao.text)
                }
            }
        }

        // Botao de limpar
        binding.cleanButton.setOnClickListener{
            textoCalculadora.text = ""
        }

        // Botao para calcular
        binding.equalButton.setOnClickListener {
            val expressao = binding.textoCalculadora.text.toString()

            try {
                val resultado = calcularExpressao(expressao)
                binding.textoCalculadora.text = resultado.toString()
            } catch (e: Exception) {
                binding.textoCalculadora.text = "Erro"
            }
        }


    }
}